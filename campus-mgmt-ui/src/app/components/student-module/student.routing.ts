import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { AttendenceComponent } from './attendence/attendence.component';
import { SubjectsComponent } from './subjects/subjects.component';
import { ExamScheduleComponent } from './exam-schedule/exam-schedule.component';
import { HomeworkComponent } from './homework/homework.component';
import { StudentRegistrationComponent } from './registration/registration.component';
import { StudentDashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'dashboard',
        component: StudentDashboardComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dashboard' },
      },
      {
        path: 'attendance',
        component: AttendenceComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Attendance' },
      },
      {
        path: 'subjects',
        component: SubjectsComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Subjects' },
      },
      {
        path: 'homework',
        component: HomeworkComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Homework' },
      },
      {
        // New route for profile with role type
        path: 'exams-schedule',
        component: ExamScheduleComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: 'Exams Schedule',
        },
      },
      {
        path: 'registration',
        component: StudentRegistrationComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Student Registration' },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StudentRoutingModule { }