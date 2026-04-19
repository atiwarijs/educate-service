import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../../services/account-service/auth.service';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css'],
  providers: [MessageService],
  imports: [SharedPrimeNgModule]
})
export class UpdatePasswordComponent {
  passwordForm: FormGroup;
  isSubmitting = false;
  showSuccessDialog = false;
  username: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
    // Redirect to login if not authenticated || this.authService.isTokenExpired()
    if (!this.authService.getToken() || this.authService.isTokenExpired()) {
      this.router.navigate(['/login/auth']);
      return;
    } else {
      this.username = this.authService.getCurrentUser()?.['preferred_username'];;
    }

    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
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
      // Only clear the passwordMismatch error
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

  /**
   * Submit the form to update the password
   */
  submit() {
    if (this.passwordForm.invalid) {
      // Mark all fields as touched to trigger validation messages
      Object.keys(this.passwordForm.controls).forEach(key => {
        this.passwordForm.get(key)?.markAsTouched();
      });
      return;
    }

    const { currentPassword, newPassword } = this.passwordForm.value;
    this.isSubmitting = true;

    this.authService.updatePassword(currentPassword, newPassword).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.showSuccessDialog = true;
      }
    });
  }

  /**
   * Navigate to login page after password update
   */
  navigateToLogin() {
    this.authService.logout(); // Log the user out to force re-login with new password
    this.router.navigate(['/login/auth']);
  }
}