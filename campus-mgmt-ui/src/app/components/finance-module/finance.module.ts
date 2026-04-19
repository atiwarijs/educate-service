import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { FinanceRoutingModule } from './finance.routing';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    FinanceRoutingModule
  ]
})
export class FinanceModule { }
