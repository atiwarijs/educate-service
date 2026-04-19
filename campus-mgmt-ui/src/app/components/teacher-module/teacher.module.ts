import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { TeacherRoutingModule } from './teacher.routing';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    TeacherRoutingModule
  ]
})
export class TeacherModule { }
