import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { StudentRoutingModule } from './student.routing';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    StudentRoutingModule
  ]
})
export class StudentModule { }
