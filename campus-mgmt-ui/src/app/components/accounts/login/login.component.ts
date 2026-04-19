// login.component.ts
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { AuthService } from '../../../services/account-service/auth.service';
import { Router, RouterModule } from '@angular/router';
import { DraftStorageService } from '../../../services/profile-service/services/draft-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [SharedPrimeNgModule, RouterModule],
  standalone: true,
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  rememberMe = false;
  errorMessage = '';
  isLoading = false;

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });

    const savedUser = this.draftStorageService.getUserProfileDraft();
    if (savedUser) {
      this.loginForm.patchValue({
        username: savedUser.username,
        password: savedUser.password || '',
        rememberMe: true
      });

      this.cd.detectChanges();
    }

    this.loginForm.get('rememberMe')?.valueChanges.subscribe((checked: boolean) => {
      const { username, password } = this.loginForm.value;
      if (checked && username && password) {
        this.draftStorageService.saveUserProfileDraft({ username, password });
      } else {
        this.draftStorageService.clearUserProfileDraft();
      }
    });
  }
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService,
    private draftStorageService: DraftStorageService,
    private cd: ChangeDetectorRef
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading = true;

      const { username, password, rememberMe } = this.loginForm.value;

      if (rememberMe) {
        this.draftStorageService.saveUserProfileDraft({ username, password });
      } else {
        this.draftStorageService.clearUserProfileDraft();
      }

      this.errorMessage = '';

      this.authService.login(username, password).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.passwordUpdateRequired === true) {
            this.messageService.add({
              severity: 'warn',
              summary: 'Update temporary password!',
              detail: 'Password Update required!',
            });
            this.router.navigate(['/account/update-password']);
          } else {
            this.router.navigate(['/dashboard']);
            this.messageService.add({
              severity: 'success',
              summary: 'Login Successful',
              detail: 'Welcome back!',
            });
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error',
            summary: 'Login Failed',
            detail: error.error?.message || 'Invalid credentials. Please try again.',
          });
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Validation Error',
        detail: 'Please check your inputs',
      });
    }
  }
  get username() {
    return this.loginForm.get('username');
  }
  get password() {
    return this.loginForm.get('password');
  }
}
