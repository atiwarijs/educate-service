import { Routes } from '@angular/router';
import { MainComponent } from './components/common/main-component/main-component.component';
import { LoginComponent } from './components/accounts/login/login.component';
import { UpdatePasswordComponent } from './components/accounts/update-password/update-password/update-password.component';
import { ResetPasswordComponent } from './components/accounts/reset-password/reset-password.component';
import { AuthGuard } from './services/account-service/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./components/common/main-component/main-component.component').then(m => m.MainComponent),
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./components/home-module/home.routing').then(m => m.HomeRoutingModule),
      },
      {
        path: 'accounts',
        loadChildren: () =>
          import('./components/accounts/accounts.module').then(m => m.AccountsModule),
      },
      {
        path: 'admin',
        loadChildren: () =>
          import('./components/admin-module/admin.module').then(m => m.AdminModule),
      },
      {
        path: 'teacher',
        loadChildren: () =>
          import('./components/teacher-module/teacher.module').then(m => m.TeacherModule),
      },
      {
        path: 'student',
        loadChildren: () =>
          import('./components/student-module/student.module').then(m => m.StudentModule),
      },
      {
        path: 'exam',
        loadChildren: () =>
          import('./components/exam-module/exam.module').then(m => m.ExamModule),
      },
      {
        path: 'notices',
        loadChildren: () =>
          import('./components/notice-module/notice.module').then(m => m.NoticeModule),
      },
      {
        path: 'finance',
        loadChildren: () =>
          import('./components/finance-module/finance.module').then(m => m.FinanceModule),
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
      // Other routes for calendar, settings, etc. can be added here
    ],
  },
  {
    path: 'login/auth',
    component: LoginComponent,
    data: { breadcrumb: 'User Login' },
  },
  {
    path: 'account/update-password',
    component: UpdatePasswordComponent,
    data: { breadcrumb: 'Update Password' },
  },
  {
    path: 'auth/reset-password',
    component: ResetPasswordComponent,
    data: { breadcrumb: 'Reset Password' },
  },
  // Removed duplicate root redirect here
  { path: '**', redirectTo: 'login/auth' },
];