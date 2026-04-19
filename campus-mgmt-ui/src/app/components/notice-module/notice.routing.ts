import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { AcademicComponent } from './academic/academic.component';
import { HolidayComponent } from './holiday/holiday.component';
import { EventsComponent } from './events/events.component';
import { AnnouncementComponent } from './announcement/announcement.component';
import { AllNoticesComponent } from './all-notices/all-notices.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'academic',
        component: AcademicComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Academic' },
      },
      {
        path: 'holidays',
        component: HolidayComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Holidays' },
      },
      {
        path: 'announcements',
        component: AnnouncementComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Announcements' },
      },
      {
        path: 'all-notices',
        component: AllNoticesComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'All Notices' },
      },
      {
        path: 'events',
        component: EventsComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: 'Events'
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NoticeRoutingModule { }