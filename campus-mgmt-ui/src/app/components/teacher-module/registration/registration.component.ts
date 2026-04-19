import { Component } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TeacherService } from '../../../services/teacher.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { SharedModule } from '../../../shared/shared-module.module';

@Component({
  selector: 'teacher-registration',
  imports: [SharedPrimeNgModule, SharedModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css',
  standalone: true
})
export class TeacherRegistrationComponent {
  teacherForm!: FormGroup;
  // genderOptions = [{ label: 'Male', value: 'Male' }, { label: 'Female', value: 'Female' }];

  constructor(private fb: FormBuilder, private teacherService: TeacherService,
    private messageService: MessageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.teacherForm = this.fb.group({
      name: ['', Validators.required],
      dob: ['', Validators.required],
      gender: ['', Validators.required],
      qualification: ['', Validators.required],
      specialization: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      joiningDate: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.teacherForm.valid) {
      const formValue = this.teacherForm.value;
      console.log('Teacher Form Data:', formValue);
      // Call backend API here
      this.teacherService.registerTeacher(formValue).subscribe({
        next: (res) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save Successful',
            detail: 'Teacher data saved successfully.',
          });
          this.teacherForm.reset();
          this.router.navigate(['/teacher/dashboard']);
        },
        error: (err) => {
          // console.error('Error registering teacher:', err);
          // alert('Failed to register teacher');
          this.messageService.add({
            severity: 'warn',
            summary: 'Registration error',
            detail: 'Failed to register teacher',
          });
        }
      });
    } else {
      this.teacherForm.markAllAsTouched();
    }
  }
}
