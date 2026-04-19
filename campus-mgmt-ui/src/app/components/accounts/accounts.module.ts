import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { AccountRoutingModule } from './accounts.routing';

@NgModule({
  declarations: [],
  imports: [CommonModule, SharedPrimeNgModule, AccountRoutingModule],
})
export class AccountsModule {}
