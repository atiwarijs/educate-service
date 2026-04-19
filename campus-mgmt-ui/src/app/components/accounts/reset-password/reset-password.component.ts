import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../services/account-service/auth.service';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
  providers: [MessageService],
  imports: [SharedPrimeNgModule, RouterModule]
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  token: string = '';
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {
    console.log('ResetPasswordComponent constructor called');
    this.resetForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    const params = this.route.snapshot.queryParams;
    console.log('Query params:', params);
    this.token = params['token'];
    console.log('Token:', this.token);
    if (!this.token) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid Reset Link',
        detail: 'The reset link is invalid or expired.'
      });
      this.router.navigate(['/login/auth']);
    }
  }

  /**
   * Custom validator to check if passwords match
   */
  passwordMatchValidator(control: AbstractControl) {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    if (newPassword !== confirmPassword) {
      control.get('confirmPassword')?.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    } else {
      const confirmErrors = control.get('confirmPassword')?.errors;
      if (confirmErrors) {
        delete confirmErrors['passwordMismatch'];
        if (Object.keys(confirmErrors).length === 0) {
          control.get('confirmPassword')?.setErrors(null);
        } else {
          control.get('confirmPassword')?.setErrors(confirmErrors);
        }
      }
      return null;
    }
  }

  submit() {
    if (this.resetForm.invalid) {
      Object.keys(this.resetForm.controls).forEach(key => {
        this.resetForm.get(key)?.markAsTouched();
      });
      return;
    }

    const { newPassword } = this.resetForm.value;
    this.isSubmitting = true;

    this.authService.resetPassword(this.token, newPassword).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        if (response.success) {
          this.messageService.add({
            severity: 'success',
            summary: 'Password Reset Successful',
            detail: 'Your password has been reset. Please log in with your new password.'
          });
          this.router.navigate(['/login/auth']);
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to reset password. The link may be expired.'
          });
        }
      },
      error: (error) => {
        this.isSubmitting = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to reset password. The link may be expired.'
        });
      }
    });
  }
}