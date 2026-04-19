import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../../services/account-service/auth.guard';
import { AccountDetailsComponent } from './account-details/account-details.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'settings/account',
        component: AccountDetailsComponent,
        data: { breadcrumb: 'Accounts Details', canActivate: [AuthGuard] },
      },
    ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
