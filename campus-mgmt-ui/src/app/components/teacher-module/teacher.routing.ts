import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { HomeworkComponent } from './homework/homework.component';
import { AssignmentsComponent } from './assignments/assignments.component';
import { AttendenceComponent } from './attendence/attendence.component';
import { TeacherRegistrationComponent } from './registration/registration.component';
import { TeacherDashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: '',
    children: [
       {
        path: 'dashboard',
        component: TeacherDashboardComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dashboard' },
      },
      {
        path: 'homework',
        component: HomeworkComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Homework' },
      },
      {
        path: 'assignments',
        component: AssignmentsComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Assignments' },
      },
      {
        path: 'registration',
        component: TeacherRegistrationComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Registration' },
      },
      {
        // New route for profile with role type
        path: 'attendance',
        component: AttendenceComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: 'Attendance'
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeacherRoutingModule { }