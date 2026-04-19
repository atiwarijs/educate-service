import { Component, OnInit, ViewChild } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { TeacherService } from '../../../services/teacher.service';
import { Table } from 'primeng/table';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'teacher-dashboard',
  imports: [SharedPrimeNgModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  standalone: true
})
export class TeacherDashboardComponent implements OnInit {

  teacherForm!: FormGroup;
  teacherList: any[];
  noDataFound: boolean = false;
  selectedTeachers: any[] = [];
  teacherDialog: boolean = false;
  teacher: any = {};
  submitted: boolean = false;
  statusOptions = [
    { label: 'Register', value: 'REGISTERED' },
    { label: 'Enroll', value: 'ENROLLED' }
  ];

  isEdit: boolean = false;

  @ViewChild('dt') dt!: Table;

  constructor(private fb: FormBuilder, private teacherService: TeacherService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }

  ngOnInit() {

    this.teacherForm = this.fb.group({
      id: [null],
      registrationNo: [''],
      employeeNo: [''],
      name: ['', Validators.required],
      dob: ['', Validators.required],
      gender: ['', Validators.required],
      qualification: [''],
      specialization: [''],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      joiningDate: ['', Validators.required],
      status: ['']
    });

    this.getTeachers();
  }

  getTeachers() {
    this.teacherService.getTeachers().subscribe({
      next: (data) => {
        this.teacherList = data;
        const filtered = (this.teacherList || []).filter(teacher =>
          teacher && (
            teacher.name?.trim() ||
            teacher.email // You can expand this condition
          )
        );

        if (filtered.length === 0) {
          this.teacherList = [];
          this.noDataFound = true;
          return;
        }
        this.noDataFound = false;
        // console.log("Teachers loaded:", this.teacherList);
        this.messageService.add({
          severity: 'success',
          summary: 'Retrieve Successful',
          detail: 'Teacher data retrieved successfully.',
        });
      }
    });
  }

  openNew() {
    this.teacher = {};
    this.submitted = false;
    this.teacherForm.reset();   // reset for new
    this.isEdit = false;
    this.teacherDialog = true;
  }

  hideDialog() {
    this.teacherDialog = false;
    this.submitted = false;
  }


  onSubmit() {
    if (this.teacherForm.valid) {
      const teacherData = this.teacherForm.value;

      this.teacherService.registerTeacher(teacherData).subscribe({
        next: (res: any) => {
          const index = this.teacherList.findIndex(t => t.id === res.id);
          if (index !== -1) {
            this.teacherList.splice(index, 1);
          }

          this.teacherList.unshift({ ...res, highlight: true });

          setTimeout(() => {
            if (this.teacherList.length) {
              this.teacherList[0].highlight = false;
            }
          }, 3000);

          this.teacherDialog = false;
          this.teacherForm.reset();

          this.messageService.add({
            severity: 'success',
            summary: 'Save Successful',
            detail: 'Teacher data saved successfully.',
          });
        },
        error: (err: Error) => {
          // console.error('Error saving teacher:', err);
          this.messageService.add({
            severity: 'warn',
            summary: 'Error',
            detail: 'Error saving teacher',
          });
        }
      });
    }
  }


  editTeacher(teacher: any) {
    this.teacherForm.patchValue(teacher);  // prefill form
    this.isEdit = true;
    this.teacherDialog = true;
  }

  deleteTeacher(teacher: any) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        this.deleteTeacherById(teacher);
      },
    });

  }

  deleteTeacherById(teacher: any) {
    this.teacherService.deleteById(teacher.id).subscribe({
      next: () => {
        this.teacherList = this.teacherList.filter(t => t.id !== teacher.id);
        this.teacher = {};
        this.messageService.add({
          severity: 'success',
          summary: 'Delete Successful',
          detail: 'Selected teacher has been deleted.',
        });
      },
      error: err => {
        this.messageService.add({
          severity: 'warn',
          summary: 'Error',
          detail: 'Error deleting teacher',
        });
      }
    });
  }

  deleteSelectedTeachers() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete selected teachers?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        const ids = this.selectedTeachers.map(t => t.id);
        this.teacherService.deleteMany(ids).subscribe({
          next: () => {
            this.teacherList = this.teacherList.filter(t => !ids.includes(t.id));
            this.selectedTeachers = [];
            this.messageService.add({
              severity: 'success',
              summary: 'Delete Successful',
              detail: 'Selected teachers have been deleted.',
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'warn',
              summary: 'Error',
              detail: 'Error deleting teacher',
            });
          }
        });
      }
    });
  }

  getSeverity(status: string): "success" | "info" | "warn" | "danger" | "secondary" | "contrast" | undefined {
    switch (status?.toUpperCase()) {
      case 'ENROLLED':
        return 'success';
      case 'REGISTERED':
        return 'warn';
      default:
        return 'secondary';
    }
  }

  getSeverityActive(isActive: boolean): "success" | "danger" | undefined {
    return isActive ? 'success' : 'danger';
  }
}