import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { SharedPrimeNgModule } from '../../shared/shared.primeng-module';
import { HomeRoutingModule } from './home.routing';


@NgModule({
    declarations: [],
    imports: [
      CommonModule,
      SharedPrimeNgModule,
      HomeRoutingModule
    ]
  })
  export class HomeModule { }
  