import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExamRoutingModule } from './exam.routing';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    ExamRoutingModule
  ]
})
export class ExamModule { }
