import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ProfileLayoutComponent } from './user-details/profile-layout/profile-layout.component';
import { ProfileSavedComponent } from './profile-view/profile-saved/profile-saved.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from '../../services/account-service/auth.guard';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dashboard' },
      },
      {
        path: 'profile-layout',
        component: ProfileLayoutComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Profile Registration' },
      },
      {
        // New route for profile with role type
        path: 'profile/:type',
        component: ProfileLayoutComponent,
        canActivate: [AuthGuard],
        data: {
          breadcrumb: (params: any) => `Profile (${params.type})`
        },
      },
      {
        path: 'profile-saved',
        component: ProfileSavedComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'User Details' },
      },
      {
        path: 'new-users',
        component: DashboardComponent, // Replace with actual component when available
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Account' },
      }
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule { }