import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  Type,
  ViewContainerRef,
  ViewChild,
  ComponentRef,
  ChangeDetectionStrategy,
  ChangeDetectorRef
} from '@angular/core';
import { FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { FamilyDetailsComponent } from '../user-details/family-details/family-details.component';
import { ExperienceComponent } from '../user-details/experience/experience.component';
import { EducationComponent } from '../user-details/education/education.component';
import { MedicalComponent } from '../user-details/medical/medical.component';
import { VehicleComponent } from '../user-details/vehicle/vehicle.component';
import { AddressComponent } from '../user-details/address/address.component';

@Component({
  selector: 'app-user-detail-popup',
  imports: [CommonModule, SharedPrimeNgModule],
  templateUrl: './user-detail-popup.component.html',
  styleUrl: './user-detail-popup.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserDetailPopupComponent implements OnInit {
  private _show = false;
  
  @Input() section: string = ''; 
  
  @Input() 
  get show() {
    return this._show;
  }
  // In user-detail-popup.component.ts
set show(value: boolean) {
  if (this._show !== value) {
    this._show = value;
    this.showChange.emit(value);
    
    // If closing the dialog, clean up component instances
    if (!value) {
      this.clearComponentInstances();
    } else if (value && this.formArray && this.childComponent) {
      // When dialog is shown, create components ONCE
      setTimeout(() => {
        if (this.componentInstances.length === 0) {
          this.createComponentInstances();
        }
      });
    }
  }
}
  
  @Output() showChange = new EventEmitter<boolean>();

  @Input() title: string = '';
  @Input() formArray!: FormArray;
  @Input() childComponent!: Type<any>;

  @Output() save = new EventEmitter<FormArray>();
  
  // Store component references to manage lifecycle
  componentInstances: any[] = [];
  
  // Use ViewChild to get content placeholders
  @ViewChild('componentsContainer', { read: ViewContainerRef, static: false }) 
  componentsContainer!: ViewContainerRef;

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    // Basic initialization
  }
  
  ngOnChanges() {
    // If dialog is open and form array or child component changes, recreate components
    if (this.show && this.formArray && this.childComponent) {
      setTimeout(() => this.createComponentInstances());
    }
  }
  
  ngAfterViewInit() {
    // Create components once view is initialized if dialog is shown
    if (this.show && this.formArray && this.childComponent) {
      setTimeout(() => this.createComponentInstances());
    }
  }

  close() {
    this.show = false;
  }

  onSave() {
    if (this.formArray?.valid) {
      this.save.emit(this.formArray);
      this.close();
    }
  }
  
  clearComponentInstances() {
    if (this.componentsContainer) {
      this.componentsContainer.clear();
      this.componentInstances = [];
    }
  }
  
  // In user-detail-popup.component.ts
  createComponentInstances() {
    // Clear existing components
    this.clearComponentInstances();
    
    if (!this.componentsContainer || !this.formArray || !this.childComponent) {
      return;
    }
    
    // Handle components that expect a full FormArray (like FamilyDetailsComponent)
    if (this.childComponent === FamilyDetailsComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['families'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    } 
    // Add other components that expect full FormArrays
    else if (this.childComponent === AddressComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['addresses'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    }
    else if (this.childComponent === VehicleComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['vehicleArray'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    }
    // And so on for other form array components
    else if (this.childComponent === MedicalComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['medicalsArray'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    }
    else if (this.childComponent === EducationComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['educationArray'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    }
    else if (this.childComponent === ExperienceComponent) {
      const componentRef = this.componentsContainer.createComponent(this.childComponent);
      componentRef.instance['experienceArray'] = this.formArray;
      this.componentInstances.push(componentRef);
      componentRef.changeDetectorRef.detectChanges();
    }
    // For other components that expect individual FormGroups
    else {
      this.formArray.controls.forEach((control, index) => {
        const componentRef = this.componentsContainer.createComponent(this.childComponent);
        const formKey = this.section + 'Form';
        componentRef.instance[formKey] = control as FormGroup;
        this.componentInstances.push(componentRef);
        componentRef.changeDetectorRef.detectChanges();
      });
    }
    
    this.cdr.markForCheck();
  }
}