// form-patching.service.ts
import { Injectable } from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormPatchService {
  
  constructor() { }
  
  /**
   * Patch date values in a form to convert strings to Date objects
   * @param control FormGroup or FormArray to patch
   * @param dateFieldNames Array of field names that contain dates
   */
  patchDateValues(control: AbstractControl, dateFieldNames: string[]): void {
    if (control instanceof FormGroup) {
      this.patchDateValuesInGroup(control, dateFieldNames);
    } else if (control instanceof FormArray) {
      control.controls.forEach(item => {
        if (item instanceof FormGroup) {
          this.patchDateValuesInGroup(item, dateFieldNames);
        }
      });
    }
  }
  
  /**
   * Patch select field values to match available options
   * @param control FormGroup or FormArray to patch
   * @param selectFieldConfig Object mapping field names to their available options
   */
  patchSelectValues(control: AbstractControl, selectFieldConfig: {[key: string]: any[]}): void {
    if (control instanceof FormGroup) {
      this.patchSelectValuesInGroup(control, selectFieldConfig);
    } else if (control instanceof FormArray) {
      control.controls.forEach(item => {
        if (item instanceof FormGroup) {
          this.patchSelectValuesInGroup(item, selectFieldConfig);
        }
      });
    }
  }
  
  /**
   * Patch radio button values to normalized values
   * @param control FormGroup or FormArray to patch
   * @param radioFieldConfig Object mapping field names to their normalized values map
   */
  patchRadioValues(control: AbstractControl, radioFieldConfig: {[key: string]: {[key: string]: string}}): void {
    if (control instanceof FormGroup) {
      this.patchRadioValuesInGroup(control, radioFieldConfig);
    } else if (control instanceof FormArray) {
      control.controls.forEach(item => {
        if (item instanceof FormGroup) {
          this.patchRadioValuesInGroup(item, radioFieldConfig);
        }
      });
    }
  }
  
  /**
   * Patch checkbox values to ensure they're properly formatted
   * @param control FormGroup or FormArray to patch
   * @param checkboxConfig Object containing checkbox field configurations
   */
  patchCheckboxValues(control: AbstractControl, checkboxConfig: {
    singleCheckboxes?: string[],                  // For single true/false checkboxes
    multiCheckboxes?: {[key: string]: string[]}   // For checkbox groups where the value is an array of selected options
  }): void {
    if (control instanceof FormGroup) {
      this.patchCheckboxValuesInGroup(control, checkboxConfig);
    } else if (control instanceof FormArray) {
      control.controls.forEach(item => {
        if (item instanceof FormGroup) {
          this.patchCheckboxValuesInGroup(item, checkboxConfig);
        }
      });
    }
  }
  
  /**
   * Apply all patching operations on a form control
   * @param control FormGroup or FormArray to patch
   * @param config Configuration object containing patching instructions
   */
  patchFormValues(control: AbstractControl, config: {
    dateFields?: string[],
    selectFields?: {[key: string]: any[]},
    radioFields?: {[key: string]: {[key: string]: string}},
    checkboxConfig?: {
      singleCheckboxes?: string[],
      multiCheckboxes?: {[key: string]: string[]}
    }
  }): void {
    if (config.dateFields) {
      this.patchDateValues(control, config.dateFields);
    }
    
    if (config.selectFields) {
      this.patchSelectValues(control, config.selectFields);
    }
    
    if (config.radioFields) {
      this.patchRadioValues(control, config.radioFields);
    }
    
    if (config.checkboxConfig) {
      this.patchCheckboxValues(control, config.checkboxConfig);
    }
  }
  
  // Private helper methods
  private patchDateValuesInGroup(group: FormGroup, dateFieldNames: string[]): void {
    dateFieldNames.forEach(fieldName => {
      const control = group.get(fieldName);
      if (control && control.value && typeof control.value === 'string') {
        control.setValue(new Date(control.value));
      }
    });
  }
  
  private patchSelectValuesInGroup(group: FormGroup, selectFieldConfig: {[key: string]: any[]}): void {
    Object.keys(selectFieldConfig).forEach(fieldName => {
      const control = group.get(fieldName);
      const options = selectFieldConfig[fieldName];
      
      if (control && control.value) {
        const value = control.value;
        
        // For object options (with label/value structure)
        if (options.length > 0 && typeof options[0] === 'object') {
          const matchingOption = options.find(opt => 
            opt.value === value || opt.label?.toLowerCase() === value.toLowerCase()
          );
          
          if (matchingOption) {
            control.setValue(matchingOption.value);
          }
        } 
        // For simple string array options
        else if (Array.isArray(options)) {
          const matchingOption = options.find(opt => 
            opt === value || opt.toLowerCase() === value.toLowerCase()
          );
          
          if (matchingOption) {
            control.setValue(matchingOption);
          }
        }
      }
    });
  }
  
  private patchRadioValuesInGroup(group: FormGroup, radioFieldConfig: {[key: string]: {[key: string]: string}}): void {
    Object.keys(radioFieldConfig).forEach(fieldName => {
      const control = group.get(fieldName);
      const valueMap = radioFieldConfig[fieldName];
      
      if (control && control.value) {
        const value = control.value.toLowerCase();
        Object.keys(valueMap).forEach(key => {
          if (value === key.toLowerCase()) {
            control.setValue(valueMap[key]);
          }
        });
      }
    });
  }
  
  private patchCheckboxValuesInGroup(group: FormGroup, checkboxConfig: {
    singleCheckboxes?: string[],
    multiCheckboxes?: {[key: string]: string[]}
  }): void {
    // Handle single checkboxes (true/false values)
    if (checkboxConfig.singleCheckboxes) {
      checkboxConfig.singleCheckboxes.forEach(fieldName => {
        const control = group.get(fieldName);
        if (control && control.value !== null && control.value !== undefined) {
          // Convert various truthy/falsy values to proper booleans
          if (typeof control.value === 'string') {
            const value = control.value.toLowerCase();
            control.setValue(
              value === 'true' || value === 'yes' || value === '1' || value === 'on'
            );
          } else {
            // Convert any non-boolean to boolean
            control.setValue(!!control.value);
          }
        }
      });
    }
    
    // Handle multi-select checkboxes (array values)
    if (checkboxConfig.multiCheckboxes) {
      Object.keys(checkboxConfig.multiCheckboxes).forEach(fieldName => {
        const control = group.get(fieldName);
        const allowedValues = checkboxConfig.multiCheckboxes![fieldName];
        
        if (control && control.value) {
          let selectedValues: string[] = [];
          
          // Handle string value that needs to be converted to array
          if (typeof control.value === 'string') {
            try {
              // Try to parse JSON string
              const parsed = JSON.parse(control.value);
              if (Array.isArray(parsed)) {
                selectedValues = parsed;
              } else {
                // If not an array, split by comma
                selectedValues = control.value.split(',').map(v => v.trim());
              }
            } catch (e) {
              // If JSON parsing fails, split by comma
              selectedValues = control.value.split(',').map(v => v.trim());
            }
          } 
          // Handle already array
          else if (Array.isArray(control.value)) {
            selectedValues = control.value;
          }
          
          // Filter to only include valid values from allowedValues
          const validValues = selectedValues.filter(value => 
            allowedValues.some(allowed => 
              allowed === value || allowed.toLowerCase() === value.toLowerCase()
            )
          );
          
          // Set the normalized values
          control.setValue(validValues);
        }
      });
    }
  }
}