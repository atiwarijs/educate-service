import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { ExamDashboardComponent } from './exam-dashboard/exam-dashboard.component';
import { ExamScheduleComponent } from './exam-schedule/exam-schedule.component';
import { ExamResultsComponent } from './exam-results/exam-results.component';
import { ExamGradesComponent } from './exam-grades/exam-grades.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'dashboard',
        component: ExamDashboardComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Exam Dashboard' },
      },
      {
        path: 'schedule',
        component: ExamScheduleComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Exam Schedule' },
      },
      {
        path: 'grades',
        component: ExamGradesComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Exam Grades' },
      },
      {
        path: 'results',
        component: ExamResultsComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: 'Exam Results'
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ExamRoutingModule { }