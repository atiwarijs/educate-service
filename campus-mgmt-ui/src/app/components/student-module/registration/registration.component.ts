import { Component } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentService } from '../../../services/student.service';
import { SharedModule } from '../../../shared/shared-module.module';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'student-registration',
  imports: [SharedPrimeNgModule, SharedModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css',
  standalone: true
})
export class StudentRegistrationComponent {
  studentForm!: FormGroup;
  genderOptions = [{ label: 'Male', value: 'Male' }, { label: 'Female', value: 'Female' }];

  constructor(private fb: FormBuilder, private studentService: StudentService,
    private messageService: MessageService,
    private router: Router) { }

  ngOnInit(): void {
    this.studentForm = this.fb.group({
      name: ['', Validators.required],
      dob: ['', Validators.required],
      gender: ['', Validators.required],
      fatherName: ['', Validators.required],
      motherName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]]
    });
  }

  onSubmit() {
    if (this.studentForm.valid) {
      const formValue = this.studentForm.value;
      console.log('Student Form Data:', formValue);
      // Call backend API here
      this.studentService.registerStudent(formValue).subscribe({
        next: (res) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save Successful',
            detail: 'Student data saved successfully.',
          });
          this.studentForm.reset();
          this.router.navigate(['/student/dashboard']);
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Registration error',
            detail: 'Failed to register student',
          });
        }
      });
    } else {
      this.studentForm.markAllAsTouched();
    }
  }
}
