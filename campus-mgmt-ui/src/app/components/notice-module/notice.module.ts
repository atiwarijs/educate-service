import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { NoticeRoutingModule } from './notice.routing';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SharedPrimeNgModule,
    NoticeRoutingModule
  ]
})
export class NoticeModule { }
