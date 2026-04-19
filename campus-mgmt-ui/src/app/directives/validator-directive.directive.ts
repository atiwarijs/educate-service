import {
  AfterViewInit,
  Directive,
  ElementRef,
  HostListener,
  Input,
  OnDestroy,
  Optional,
  Renderer2,
} from '@angular/core';
import { FormGroupDirective, NgControl } from '@angular/forms';
import { Subscription } from 'rxjs';
import { FormSubmissionService } from '../services/form-submission.service';

@Directive({
  selector: '[appFormValidator]',
  standalone: true,
})
export class FormValidatorDirective implements AfterViewInit, OnDestroy {
  private errorEl: HTMLElement;
  private subscriptions: Subscription[] = [];
  private formSubmitted = false;
  private componentType: string = 'standard';

  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
    private control: NgControl,
    @Optional() private formGroup: FormGroupDirective,
    private formSubmissionService: FormSubmissionService
  ) {
    this.subscriptions.push(
      this.formSubmissionService.formSubmit$.subscribe(() => {
        this.formSubmitted = true;
        this.updateFeedback();
      })
    );
    
    this.errorEl = this.renderer.createElement('small');
    this.renderer.addClass(this.errorEl, 'p-error');
    this.renderer.setStyle(this.errorEl, 'display', 'none');
    this.renderer.setStyle(this.errorEl, 'font-size', '0.75rem');
    this.renderer.setStyle(this.errorEl, 'color', '#f44336');
  }

  ngAfterViewInit(): void {
    // Detect component type based on tag name
    const tagName = this.el.nativeElement.tagName.toLowerCase();
    this.componentType = tagName;
    
    // Log component structure in development for debugging
    // this.debugComponentStructure();
    
    const parent = this.el.nativeElement.parentNode;
    this.renderer.appendChild(parent, this.errorEl);

    // Subscribe to control status changes
    if (this.control.control?.statusChanges) {
      this.subscriptions.push(
        this.control.control.statusChanges.subscribe(() => {
          this.updateFeedback();
        })
      );
    }

    // Subscribe to control value changes
    if (this.control.control?.valueChanges) {
      this.subscriptions.push(
        this.control.control.valueChanges.subscribe(() => {
          this.updateFeedback();
        })
      );
    }

    // Subscribe to form submit events
    if (this.formGroup) {
      this.subscriptions.push(
        this.formGroup.ngSubmit.subscribe(() => {
          this.formSubmitted = true;
          this.updateFeedback();
        })
      );
    }

    // Initialize with current state after a short delay
    setTimeout(() => {
      this.updateFeedback();
    }, 0);
  }

  // Debug function to log component structure - can be removed in production
  // private debugComponentStructure() {
  //   console.log('Component tag:', this.el.nativeElement.tagName);
  //   console.log('Component children:', this.el.nativeElement.children);
    
  //   Array.from(this.el.nativeElement.querySelectorAll('*')).forEach((el:any) => {
  //     console.log(`Element: ${el.tagName}, Classes: ${el.className}`);
  //   });
  // }

  // Listen to blur events to mark as touched
  @HostListener('blur')
  onBlur() {
    if (this.control.control) {
      this.control.control.markAsTouched();
      this.updateFeedback();
    }
  }

  // For PrimeNG components, listen to their specific events
  @HostListener('onBlur')
  @HostListener('onHide')
  @HostListener('onChange')
  onPrimeControlEvent() {
    if (this.componentType === 'p-select' || this.componentType === 'p-datepicker' || this.componentType === 'p-radiobutton') {
      if (this.control.control) {
        this.control.control.markAsTouched();
        this.updateFeedback();
      }
    }
  }

  updateFeedback() {
    const c = this.control.control;
    if (!c) return;

    // Consider the control invalid if:
    // 1. It's invalid AND (it's touched OR dirty OR the form was submitted)
    const invalid = c.invalid && (c.touched || c.dirty || this.formSubmitted);

    if (invalid) {
      // Add base invalid class to the component
      this.renderer.addClass(this.el.nativeElement, 'ng-invalid');
      
      // Apply specific styling based on component type
      if (this.componentType === 'p-select') {
        // Style the select component
        this.renderer.addClass(this.el.nativeElement, 'p-invalid');
        
        // Style the dropdown button
        const dropdownEl = this.el.nativeElement.querySelector('.p-select-dropdown');
        if (dropdownEl) {
          this.renderer.addClass(dropdownEl, 'p-invalid');
          this.renderer.setStyle(dropdownEl, 'border-color', '#f44336');
        }
        
        // Style the label
        const labelEl = this.el.nativeElement.querySelector('.p-select-label');
        if (labelEl) {
          this.renderer.addClass(labelEl, 'p-invalid');
        }
        
        // Add a custom attribute for CSS targeting
        this.renderer.setAttribute(this.el.nativeElement, 'data-invalid', 'true');
      } 
      else if (this.componentType === 'p-datepicker') {
        // Style the datepicker component
        this.renderer.addClass(this.el.nativeElement, 'p-invalid');
        
        // Find the calendar container
        const calendarEl = this.el.nativeElement.querySelector('.p-calendar');
        if (calendarEl) {
          this.renderer.addClass(calendarEl, 'p-invalid');
          this.renderer.setStyle(calendarEl, 'border-color', '#f44336');
        }
        
        // Find the input element
        const inputEl = this.el.nativeElement.querySelector('input');
        if (inputEl) {
          this.renderer.addClass(inputEl, 'p-invalid');
          this.renderer.setStyle(inputEl, 'border-color', '#f44336');
        }
        
        this.renderer.setAttribute(this.el.nativeElement, 'data-invalid', 'true');
      }
      else if (this.componentType === 'p-radiobutton') {
        // Style the radio button component
        this.renderer.addClass(this.el.nativeElement, 'p-invalid');
        
        // Find the radio button element
        const radioEl = this.el.nativeElement.querySelector('.p-radiobutton');
        if (radioEl) {
          this.renderer.addClass(radioEl, 'p-invalid');
        }
      }
      else {
        // Standard input handling
        this.renderer.addClass(this.el.nativeElement, 'p-invalid');
      }
      
      // Show error message
      this.renderer.setStyle(this.errorEl, 'display', 'block');
      this.errorEl.textContent = this.getErrorMessage(c.errors);
    } else {
      // Remove invalid styling
      this.renderer.removeClass(this.el.nativeElement, 'ng-invalid');
      this.renderer.removeClass(this.el.nativeElement, 'p-invalid');
      
      // Remove specific styling based on component type
      if (this.componentType === 'p-select') {
        const dropdownEl = this.el.nativeElement.querySelector('.p-select-dropdown');
        if (dropdownEl) {
          this.renderer.removeClass(dropdownEl, 'p-invalid');
          this.renderer.removeStyle(dropdownEl, 'border-color');
        }
        
        const labelEl = this.el.nativeElement.querySelector('.p-select-label');
        if (labelEl) {
          this.renderer.removeClass(labelEl, 'p-invalid');
        }
        
        this.renderer.removeAttribute(this.el.nativeElement, 'data-invalid');
      } 
      else if (this.componentType === 'p-datepicker') {
        const calendarEl = this.el.nativeElement.querySelector('.p-calendar');
        if (calendarEl) {
          this.renderer.removeClass(calendarEl, 'p-invalid');
          this.renderer.removeStyle(calendarEl, 'border-color');
        }
        
        const inputEl = this.el.nativeElement.querySelector('input');
        if (inputEl) {
          this.renderer.removeClass(inputEl, 'p-invalid');
          this.renderer.removeStyle(inputEl, 'border-color');
        }
        
        this.renderer.removeAttribute(this.el.nativeElement, 'data-invalid');
      }
      else if (this.componentType === 'p-radiobutton') {
        const radioEl = this.el.nativeElement.querySelector('.p-radiobutton');
        if (radioEl) {
          this.renderer.removeClass(radioEl, 'p-invalid');
        }
      }
      
      // Hide error message
      this.renderer.setStyle(this.errorEl, 'display', 'none');
    }
  }

  getErrorMessage(errors: any): string {
    if (!errors) return '';
    if (errors['required']) return 'Required';
    if (errors['email']) return 'Invalid email address';
    if (errors['minlength'])
      return `Minimum ${errors['minlength'].requiredLength} characters required`;
    if (errors['maxlength'])
      return `Maximum ${errors['maxlength'].requiredLength} characters allowed`;
    if (errors['pattern']) {
      if (errors['pattern'].requiredPattern === '^[0-9]*$')
        return 'Only numbers allowed';
      if (errors['pattern'].requiredPattern === '^[a-zA-Z ]*$')
        return 'Only letters allowed';
      return 'Invalid format';
    }
    if (errors['min']) return `Minimum value is ${errors['min'].min}`;
    if (errors['max']) return `Maximum value is ${errors['max'].max}`;
    return 'Invalid input';
  }

  ngOnDestroy(): void {
    // Clean up all subscriptions
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }
}