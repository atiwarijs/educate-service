import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { TeacherListComponent } from './teacher-list/teacher-list.component';
import { StudentListComponent } from './student-list/student-list.component';
import { ClassSectionComponent } from './class-section/class-section.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'dashboard',
        component: AdminDashboardComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Admin Dashboard' },
      },
      {
        path: 'teachers',
        component: TeacherListComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Teachers' },
      },
      {
        path: 'students',
        component: StudentListComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Students' },
      },
      {
        path: 'class-section',
        component: ClassSectionComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Class/Section' },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule { }