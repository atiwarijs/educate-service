import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { ClassesService } from '../../../services/classes.service';
import { forkJoin, tap } from 'rxjs';

// interface ClassModel {
//   id?: number;
//   className: string;
//   section: string;
//   academicYear: string;
//   classTeacher?: string;
//   strength?: number;
//   status: string;
// }

@Component({
  selector: 'app-class-section',
  imports: [SharedPrimeNgModule],
  templateUrl: './class-section.component.html',
  styleUrl: './class-section.component.css',
  standalone: true
})
export class ClassSectionComponent implements OnInit {

  classList: any[] = [];
  selectedClasses: any[] = [];
  classDialog: boolean = false;
  isEdit: boolean = false;

  sectionDialogVisible = false;
  selectedClass: any;

  classForm!: FormGroup;
  submitted: boolean = false;
  noDataFound: boolean = false;

  // Dropdown options
  statusOptions = [
    { label: 'Operational', value: 'Operational' },
    { label: 'In-Operational', value: 'In-Operational' }
  ];

  classOptions = [
    { label: 'Nursery', value: 'Nursery' },
    { label: 'KG1', value: 'KG1' },
    { label: 'KG2', value: 'KG2' },
    { label: 'Class 1', value: '1' },
    { label: 'Class 2', value: '2' },
    { label: 'Class 3', value: '3' },
    { label: 'Class 4', value: '4' },
    { label: 'Class 5', value: '5' },
    { label: 'Class 6', value: '6' },
    { label: 'Class 7', value: '7' },
    { label: 'Class 8', value: '8' },
    { label: 'Class 9', value: '9' },
    { label: 'Class 10', value: '10' },
    { label: 'Class 11', value: '11' },
    { label: 'Class 12', value: '12' }
  ];

  sections = [
    { label: 'A', value: 'A' },
    { label: 'B', value: 'B' },
    { label: 'C', value: 'C' },
    { label: 'D', value: 'D' },
    { label: 'E', value: 'E' },
    { label: 'F', value: 'F' },
    { label: 'G', value: 'G' },
    { label: 'H', value: 'H' },
    { label: 'I', value: 'I' },
    { label: 'J', value: 'J' },
    { label: 'K', value: 'K' },
    { label: 'L', value: 'L' }
  ];

  totalStrength = 0;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private classService: ClassesService
  ) { }

  ngOnInit(): void {
    this.loadClasses();

    this.classForm = this.fb.group({
      id: [],
      className: ['', Validators.required],
      sections: this.fb.array([]),
      academicYear: [this.getCurrentAcademicYear(), Validators.required],
      selectedSections: [[]],
      status: ['In-Operational', Validators.required]
    });
  }

  get sectionsFormArray(): FormArray {
    return this.classForm.get('sections') as FormArray;
  }

  onSectionChange(selectedValues: string[]) {
    this.classForm.patchValue({ selectedSections: selectedValues });

    // preserve already-entered strengths by section
    const existing = new Map<string, number>();
    this.sectionsFormArray.controls.forEach(ctrl => {
      const sec = ctrl.get('sectionName')?.value;
      const str = Number(ctrl.get('strength')?.value ?? 0);
      if (sec) existing.set(sec, str);
    });

    // rebuild form array to match selection order
    this.sectionsFormArray.clear();
    selectedValues.forEach(sec => {
      this.sectionsFormArray.push(
        this.fb.group({
          sectionName: [sec],
          strength: [existing.get(sec) ?? 0, [Validators.min(10)]]
        })
      );
    });

    this.recalcTotalStrength();
  }

  private getCurrentAcademicYear(): string {
    const currentYear = new Date().getFullYear();
    const nextYear = currentYear + 1;
    return `${currentYear}-${nextYear}`;
  }


  // Load classes from API (replace with service call)
  loadClasses(showMessage: boolean = false) {
    this.classService.getClasses().subscribe({
      next: (res) => {

        this.classList = res;
        const filtered = (this.classList || []).filter((classObj: any) =>
          classObj && (
            classObj.className?.trim()
          )
        );

        if (filtered.length === 0) {
          this.noDataFound = true;
        } else {
          this.noDataFound = false;
          if (showMessage) {
            this.messageService.add({
              severity: 'success',
              summary: 'Retrieved',
              detail: 'Class data retrieved successfully.',
            });
          }
        }
      },
      error: () => {
        this.noDataFound = true;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load class data.',
        });
      }
    });
  }


  openNew() {
    this.classForm.reset();
    this.sectionsFormArray.clear();
    this.totalStrength = 0;

    this.classForm.patchValue({
      academicYear: this.getCurrentAcademicYear(),
      status: 'In-Operational',
      selectedSections: []
    });

    this.isEdit = false;
    this.classDialog = true;
    this.submitted = false;

    this.watchStrengthChangesOnce();
  }

  /** Ensure selected sections & strengths populate correctly */
  editClass(classObj: any) {
    this.isEdit = true;
    this.classDialog = true;
    this.submitted = false;

    // Patch basics
    this.classForm.patchValue({
      id: classObj.id,
      className: classObj.className,             // value must match classOptions.value
      academicYear: classObj.academicYear,
      status: classObj.status
    });

    // Build selectedSections + sections FormArray
    const incoming = Array.isArray(classObj.sections) ? classObj.sections : []; // [{section, strength}]
    const selectedValues = incoming.map((s: any) => s.sectionName);
    this.classForm.patchValue({ selectedSections: selectedValues });
    this.sectionsFormArray.clear();
    incoming.forEach((s: any) => {
      this.sectionsFormArray.push(
        this.fb.group({
          sectionName: [s.sectionName],
          strength: [s.strength ?? 0, [Validators.min(0)]]
        })
      );
    });

    this.recalcTotalStrength();
    this.watchStrengthChangesOnce();
  }

  recalcTotalStrength() {
    this.totalStrength = this.sectionsFormArray.controls
      .map(ctrl => Number(ctrl.get('strength')?.value ?? 0))
      .reduce((a, b) => a + b, 0);
  }

  /** Hook valueChanges to auto-recalc totals */
  watchStrengthChangesOnce() {
    // subscribe once (call in openNew & editClass after building the array)
    this.sectionsFormArray.valueChanges.subscribe(() => this.recalcTotalStrength());
  }

  hideDialog() {
    this.classDialog = false;
    this.submitted = false;
  }

  onSubmit() {
    if (this.classForm.valid) {
      const formValue = this.classForm.value;

      // prepare payload: sections[] already has section+strength
      const payload = {
        ...formValue,
        sections: formValue.sections
      };

      if (this.isEdit) {
        this.classService.updateClass(payload).subscribe(() => {
          this.loadClasses(true);
          this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Class updated successfully' });
          this.classDialog = false;
        });
      } else {
        this.classService.registerClass(payload).subscribe(() => {
          this.loadClasses(true);
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Class added successfully' });
          this.classDialog = false;
        });
      }
    }
  }

  deleteClass(classObj: any) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.classService.deleteById(classObj.id).subscribe(() => {
          this.classList = this.classList.filter(c => c.id !== classObj.id);
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Class deleted successfully' });
        });
      }
    });
  }

  deleteSelectedClasses() {
    this.confirmationService.confirm({
      message: 'Delete selected classes?',
      header: 'Confirm',
      accept: () => {
        const deleteRequests = this.selectedClasses
          .filter(classObj => classObj?.id !== undefined)
          .map(classObj =>
            this.classService.deleteById(classObj?.id!).pipe(
              tap(() => {
                this.classList = this.classList.filter(c => c.id !== classObj.id);
              })
            )
          );

        if (deleteRequests.length > 0) {
          // Run all delete requests in parallel
          forkJoin(deleteRequests).subscribe(() => {
            this.selectedClasses = [];
            this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Selected classes deleted' });
          });
        } else {
          this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'No valid class IDs to delete' });
        }
      }
    });
  }



  // Severity styling for status
  getSeverity(status: string): "success" | "info" | "warn" | "danger" | "secondary" | "contrast" | undefined {
    switch (status) {
      case 'Operational':
        return 'success';
      case 'In-Operational':
        return 'danger';
      default:
        return 'info';
    }
  }

  openSectionsDialog(classData: any) {
    this.selectedClass = classData;
    this.sectionDialogVisible = true;
  }
}
