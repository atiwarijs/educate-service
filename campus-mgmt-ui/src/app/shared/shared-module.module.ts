import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormValidatorDirective } from '../directives/validator-directive.directive';
import { LoaderComponent } from '../components/common/loader/loader.component';

@NgModule({
  declarations: [], 
  imports: [
    CommonModule,
    FormValidatorDirective,
    LoaderComponent
  ],
  exports: [
    FormValidatorDirective ,
    
  ],
})
export class SharedModule {}