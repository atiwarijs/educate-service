import { Component } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { StudentService } from '../../../services/student.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ClassesService } from '../../../services/classes.service';

@Component({
  selector: 'student-dashboard',
  imports: [SharedPrimeNgModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  standalone: true
})
export class StudentDashboardComponent {
  studentList: any[] = [];
  selectedStudents: any[] = [];
  studentDialog = false;
  studentForm!: FormGroup;
  isEdit = false;
  noDataFound = false;

  classOptions: any[] = [];
  sectionOptions: any[] = [];

  statusOptions = [
    { label: 'Registered', value: 'REGISTERED' },
    { label: 'Enrolled', value: 'ENROLLED' }
  ];

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private classService: ClassesService
  ) { }

  ngOnInit() {
    this.loadStudents();
    this.loadClasses();
    this.studentForm = this.fb.group({
      id: [null],
      enrolmentNo: [''],
      registrationNo: [''],
      name: ['', Validators.required],
      dob: ['', Validators.required],
      gender: ['', Validators.required],
      fatherName: [''],
      motherName: [''],
      email: ['', [Validators.required, Validators.email]],
      status: ['REGISTERED'],
      assignClass: [false],          // checkbox
      classId: [null],               // dropdown value
      sectionId: [null],
      sections: this.fb.array([])
    });
  }

  loadStudents() {
    this.studentService.getStudents().subscribe((res) => {
      this.studentList = res;
      const filtered = (this.studentList || []).filter(student =>
        student && (
          student.name?.trim() ||
          student.email // You can expand this condition
        )
      ).map(student => ({
        ...student,
        assignClass: !!(student.classId || student.sectionId) // 👈 add flag
      }));

      if (filtered.length === 0) {
        this.studentList = [];
        this.noDataFound = true;
        return;
      }
      this.noDataFound = false;
      this.messageService.add({
        severity: 'success',
        summary: 'Retrieve Successful',
        detail: 'Student data retrieved successfully.',
      });
    });

  }

  openNew() {
    this.studentForm.reset({ status: 'REGISTERED' });
    this.isEdit = false;
    this.studentDialog = true;
  }

  editStudent(student: any) {
    if (!student) return;
    this.studentForm.patchValue(student);
    if (student.sections && student.sections.length > 0) {
      this.sectionOptions = student.sections.map((s: any) => ({
        id: s.id,
        label: `${s.sectionName} (Strength: ${s.strength})`,
        value: s.id
      }));
      if (student.sectionId) {
        this.studentForm.get('sectionId')?.setValue(student.sectionId);
      }
    } else {
      this.sectionOptions = [];
      this.studentForm.get('sectionId')?.setValue(null);
    }
    this.isEdit = true;
    this.studentDialog = true;
  }


  hideDialog() {
    this.studentDialog = false;
  }

  onSubmit() {
    if (this.studentForm.valid) {
      const studentData = { ...this.studentForm.value };

      if (!studentData.assignClass) {
        studentData.classId = null;
        studentData.sectionId = null;
      }
      if (this.isEdit) {
        this.studentService.updateStudent(studentData).subscribe(() => {
          this.loadStudents();
          this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Student updated successfully' });
          this.studentDialog = false;
        });
      } else {
        this.studentService.registerStudent(studentData).subscribe(() => {
          this.loadStudents();
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Student added successfully' });
          this.studentDialog = false;
        });
      }
    }
  }

  onAssignClassChange(event: any) {
    if (!event.checked) {
      this.studentForm.patchValue({
        classId: null,
        sectionId: null
      });
      this.sectionOptions = []; // clear sections too
    }
  }


  deleteStudent(student: any) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.studentService.deleteById(student.id).subscribe(() => {
          this.studentList = this.studentList.filter(s => s.id !== student.id);
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Student deleted successfully' });
        });
      }
    });
  }

  deleteSelectedStudents() {
    this.confirmationService.confirm({
      message: 'Delete selected students?',
      header: 'Confirm',
      accept: () => {
        this.selectedStudents.forEach(student => {
          this.studentService.deleteById(student.id).subscribe();
        });
        this.studentList = this.studentList.filter(s => !this.selectedStudents.includes(s));
        this.selectedStudents = [];
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Selected students deleted' });
      }
    });
  }

  getSeverity(status: string): 'success' | 'info' | 'warn' | 'danger' {
    switch (status) {
      case 'REGISTERED': return 'info';
      case 'ENROLLED': return 'success';
      default: return 'warn';
    }
  }

  loadClasses(showMessage: boolean = false) {
    this.classService.getClasses().subscribe({
      next: (res) => {

        this.classOptions = res;
        const filtered = (this.classOptions || []).filter((classObj: any) =>
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

  onClassChange(classId: number) {
    const selectedClass = this.classOptions.find(c => c.id === classId);
    if (selectedClass) {
      this.sectionOptions = selectedClass.sections.map((s: any) => ({
        id: s.id,
        label: `${s.sectionName} (Strength: ${s.strength})`, // 👈 combined
        value: s.id
      }));
    } else {
      this.sectionOptions = [];
    }
  }

}
