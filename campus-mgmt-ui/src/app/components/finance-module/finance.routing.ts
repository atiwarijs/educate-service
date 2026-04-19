import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { SchoolExpensesComponent } from './school-expenses/school-expenses.component';
import { FeesManagementComponent } from './fees-management/fees-management.component';
import { EventsExpensesComponent } from './events-expenses/events-expenses.component';
import { TransportExpensesComponent } from './transport-expenses/transport-expenses.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'school-expenses',
        component: SchoolExpensesComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'School Expenses' },
      },
      {
        path: 'fees-management',
        component: FeesManagementComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Fees Management' },
      },
      {
        path: 'transport-expenses',
        component: TransportExpensesComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Transport Expenses' },
      },
      {
        path: 'exam-expenses',
        component: EventsExpensesComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Exam Expenses' },
      },
      {
        path: 'events-expenses',
        component: EventsExpensesComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: 'Events Expenses'
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FinanceRoutingModule { }