import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { AdminRoutingModule } from './admin.routing';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
