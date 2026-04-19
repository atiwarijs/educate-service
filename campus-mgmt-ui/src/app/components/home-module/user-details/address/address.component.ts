import {
  ChangeDetectorRef,
  Component,
  Inject,
  Input,
  OnInit,
  Optional,
  SimpleChanges,
} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { ConfirmationService } from 'primeng/api';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { CheckboxChangeEvent } from 'primeng/checkbox';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { SharedModule } from '../../../../shared/shared-module.module';
import { UserApiService } from '../../../../services/profile-service/services/user-api.service';
import { LocationService } from '../../../../services/profile-service/location-service/location.service';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-address',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  standalone: true,
  templateUrl: './address.component.html',
  styleUrl: './address.component.css',
})
export class AddressComponent implements OnInit {
  @Input() addresses!: FormArray;
  selectedIndex: number = 0;
  copyPermanentToCorrespondence: boolean = false;
  sameAddressControl = new FormControl(false);

  statusOptions = ['Active', 'Inactive'];
  cityList: any[] = [];

  // Expanded address options for additional addresses
  addressOptions = [
    // { label: 'Permanent', value: 'Permanent' },
    // { label: 'Correspondence', value: 'Correspondence' },
    { label: 'Office', value: 'Office' },
    { label: 'Temporary', value: 'Temporary' },
    { label: 'Parent', value: 'Parent' },
    { label: 'Other', value: 'Other' },
  ];

  documentOptions = [
    { label: 'Aadhar', value: 'Aadhar' },
    { label: 'Rent Aggreement', value: 'Rent Aggreement' },
    { label: 'Water Bill', value: 'Water' },
    { label: 'Electricity Bill', value: 'Electricity' },
    { label: 'Broadband Bill', value: 'Broadband' },
  ];

  stateDetails: any = [];

  ngOnInit(): void {
    this.selectedIndex = 0;
    if (!this.addresses || this.addresses.length === 0) {
      this.addInitialAddresses();
    } else {
      // Ensure the first two addresses are Permanent and Correspondence
      this.ensureRequiredAddressTypes();
    }
    this.getAllStateList();
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['addresses'] && changes['addresses'].currentValue) {
      this.patchFormValues();
    }
  }
  
  private patchFormValues(): void {
    if (!this.addresses) return;
  
    if (this.addresses instanceof FormArray) {
      this.addresses.controls.forEach((addressControl, index) => {
        if (addressControl instanceof FormGroup) {
          // First get all states to ensure they're loaded
          this.getAllStateList().then(() => {
            // Now patch state and city for this address
            this.patchStateAndCity(addressControl);
          });
        }
      });
    }
  }
  
  private getAllStateList(): Promise<void> {
    // Convert to Promise for easier chaining
    return new Promise<void>((resolve) => {
      // Only fetch if not already loaded
      if (this.stateDetails && this.stateDetails.length > 0) {
        resolve();
        return;
      }
  
      this.locationService.getAllStateList().subscribe({
        next: (userData: any[] | null) => {
          this.stateDetails = userData || [];
          console.log('states list:', this.stateDetails);
          resolve();
        },
        error: () => {
          // Handle error but still resolve to continue execution
          console.error('Failed to load states');
          resolve();
        }
      });
    });
  }
  
  private patchStateAndCity(addressGroup: FormGroup): void {
    const stateControl = addressGroup.get('state');
    const cityControl = addressGroup.get('city');
    
    if (!stateControl || !this.stateDetails || this.stateDetails.length === 0) return;
  
    // Get the current value from the control (might be ID or name)
    const stateValue = stateControl.value;
    if (!stateValue) return;
    
    // Try matching by id first, then by name
    let selectedState = this.stateDetails.find((s: { id: any; }) => s.id === stateValue);
    
    // If no match by ID, try by name
    if (!selectedState) {
      selectedState = this.stateDetails.find(
        (s: { name: string; }) => s.name.toLowerCase() === String(stateValue).toLowerCase()
      );
    }
    
    if (selectedState) {
      // Set the correct value format (ID) in the form
      stateControl.setValue(selectedState.id, {emitEvent: false});
      
      // Populate city list
      const cities = selectedState.cities || [];
      this.cityList = cities;
      
      // Now patch the city if it exists
      if (cityControl && cityControl.value) {
        const cityValue = cityControl.value;
        
        // Try matching city by id first, then by name
        let selectedCity = cities.find((c: { id: any; }) => c.id === cityValue);
        
        // If no match by ID, try by name
        if (!selectedCity) {
          selectedCity = cities.find(
            (c: { name: string; }) => c.name.toLowerCase() === String(cityValue).toLowerCase()
          );
        }
        
        if (selectedCity) {
          cityControl.setValue(selectedCity.id, {emitEvent: false});
        }
      }
    }
  }
  
  // Modified to work with a specific address FormGroup
  // private onStateChangeForAddress(stateId: number, addressGroup: FormGroup): void {
  //   const selectedState = this.stateDetails.find((s: { id: number; }) => s.id === stateId);
    
  //   // Store cities for this specific address form group
  //   const cities = selectedState?.cities || [];
    
  //   // You can either store city options in a component-wide property:
  //   // this.cityList = cities;
    
  //   // Or attach it to the form group as a custom property if needed
  //   (addressGroup as any).cityOptions = cities;
    
  //   // Reset city value if no match is found
  //   const cityControl = addressGroup.get('city');
  //   if (cityControl && cities.length > 0) {
  //     const currentCityValue = cityControl.value;
  //     const cityExists = cities.some((c: { id: any; name: any; }) => c.id === currentCityValue || c.name === currentCityValue);
  //     if (!cityExists) {
  //       cityControl.setValue('');
  //     }
  //   }
  // }

  constructor(
    @Optional() @Inject('addresses') injectedForm: FormArray,
    private userFormService: UserFormsService,
    private confirmationService: ConfirmationService,
    private fileUploadService: FileUploadService,
    private cdr: ChangeDetectorRef,
    private userApiService: UserApiService,
    private locationService: LocationService,
    private formPatchingService: FormPatchService
  ) {
    if (injectedForm) {
      this.addresses = injectedForm;
    }
  }

  // private getAllStateList() {
  //   this.locationService.getAllStateList().subscribe({
  //     next: (userData: any[] | null) => {
  //       this.stateDetails = userData;
  //       console.log('states list:', this.stateDetails);
  //     },
  //   });
  // }

  public onStateChange(stateId: string) {
    const selectedState = this.stateDetails.find(
      (s: { id: string }) => s.id === stateId
    );
    this.cityList = selectedState?.cities || [];
  }

  // Add Permanent and Correspondence addresses if they don't exist
  private addInitialAddresses(): void {
    // Create permanent address
    const permanentAddressGroup = this.userFormService.createAddressGroup(
      {} as any
    );
    permanentAddressGroup.removeControl('addressType');
    permanentAddressGroup.addControl(
      'addressType',
      new FormControl({
        value: 'Permanent',
        disabled: true,
      })
    );
    permanentAddressGroup.patchValue({ status: 'Active' });
    this.addresses.push(permanentAddressGroup);

    // Add Correspondence address
    const correspondenceAddressGroup = this.userFormService.createAddressGroup(
      {} as any
    );
    correspondenceAddressGroup.removeControl('addressType');
    correspondenceAddressGroup.addControl(
      'addressType',
      new FormControl({
        value: 'Correspondence',
        disabled: true,
      })
    );
    correspondenceAddressGroup.patchValue({ status: 'Active' });
    this.addresses.push(correspondenceAddressGroup);
  }

  // Ensure the first two addresses are Permanent and Correspondence
  private ensureRequiredAddressTypes(): void {
    // Check and set Permanent address
    if (this.addresses.length >= 1) {
      const firstAddress = this.addresses.at(0) as FormGroup;
      const addressTypeControl = firstAddress.get('addressType');

      if (addressTypeControl) {
        // If control exists, update its value and disable it
        if (addressTypeControl.value !== 'Permanent') {
          addressTypeControl.setValue('Permanent');
        }

        if (!addressTypeControl.disabled) {
          addressTypeControl.disable();
        }
      } else {
        // If control doesn't exist, create it as disabled
        firstAddress.addControl(
          'addressType',
          new FormControl({
            value: 'Permanent',
            disabled: true,
          })
        );
      }
    }

    // Check and set Correspondence address
    if (this.addresses.length >= 2) {
      const secondAddress = this.addresses.at(1) as FormGroup;
      const addressTypeControl = secondAddress.get('addressType');

      if (addressTypeControl) {
        // If control exists, update its value and disable it
        if (addressTypeControl.value !== 'Correspondence') {
          addressTypeControl.setValue('Correspondence');
        }

        if (!addressTypeControl.disabled) {
          addressTypeControl.disable();
        }
      } else {
        // If control doesn't exist, create it as disabled
        secondAddress.addControl(
          'addressType',
          new FormControl({
            value: 'Correspondence',
            disabled: true,
          })
        );
      }
    }
  }

  onCheckboxToggle(event: CheckboxChangeEvent): void {
    this.copyPermanentToCorrespondence = event.checked;

    if (event.checked) {
      const permanentValue = { ...this.permanentAddress.getRawValue() }; // Use getRawValue to get disabled values
      permanentValue.addressType = 'Correspondence';
      this.correspondenceAddress.patchValue(permanentValue);

      // Disable the correspondence address
      const addressTypeControl = this.correspondenceAddress.get('addressType');
      const addressTypeValue = addressTypeControl?.value || 'Correspondence';
      this.correspondenceAddress.disable();

      // Make sure addressType is still set to 'Correspondence'
      if (addressTypeControl) {
        addressTypeControl.setValue('Correspondence');
      }
    } else {
      // Reset fields, preserve addressType as Correspondence
      const addressTypeControl = this.correspondenceAddress.get('addressType');
      const addressTypeValue = addressTypeControl?.value || 'Correspondence';

      this.correspondenceAddress.enable();
      this.correspondenceAddress.reset({ status: 'Active' });

      // Re-disable and set addressType
      if (addressTypeControl) {
        addressTypeControl.setValue('Correspondence');
        addressTypeControl.disable();
      }
    }
  }

  get permanentAddress(): FormGroup {
    return this.addresses.at(0) as FormGroup;
  }

  get correspondenceAddress(): FormGroup {
    return this.addresses.at(1) as FormGroup;
  }

  isPermanentAddress(index: number): boolean {
    return (
      index === 0 ||
      this.addresses.at(index).get('addressType')?.value === 'Permanent'
    );
  }

  isCorrespondenceAddress(index: number): boolean {
    return (
      index === 1 ||
      this.addresses.at(index).get('addressType')?.value === 'Correspondence'
    );
  }

  addAddress() {
    const newAddress = this.userFormService.createAddressGroup({} as any);
    newAddress.patchValue({ status: 'Active' });
    this.addresses.push(newAddress);
    this.selectedIndex = this.addresses.length - 1;
  }

  // Methods to remove items from form arrays
  removeAddress(index: number) {
    if (this.addresses.length > 2 && index >= 2) {
      // Don't allow removal of Permanent and Correspondence
      this.addresses.removeAt(index);
      this.selectedIndex = Math.min(
        this.selectedIndex,
        this.addresses.length - 1
      );
    }
  }

  getDocumentsArray(addressIndex: number): FormArray {
    const address = this.addresses.at(addressIndex) as FormGroup;
    return address.get('addressDocuments') as FormArray;
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  confirmDelete(index: number): void {
    // Only allow deletion of non-permanent and non-correspondence addresses
    if (index < 2) return;

    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        this.removeAddress(index);
      },
    });
  }

  // Get available address types (excluding ones already used)
  // getAvailableAddressTypes(): any[] {
  //   // Get all currently used address types (including disabled controls)
  //   const usedTypes = this.addresses.controls.map((control) => {
  //     const group = control as FormGroup;
  //     const addressTypeControl = group.get('addressType');
  //     // Use getRawValue or directly access value of disabled control
  //     return addressTypeControl?.value || addressTypeControl?.getRawValue?.();
  //   });

  //   // Return only address types that aren't already used
  //   return this.addressOptions.filter(
  //     (option) => !usedTypes.includes(option.value)
  //   );
  // }

  // getAvailableDocTypes(addressIndex: number, currentDocIndex: number): any[] {
  //   const documentsArray = this.getDocumentsArray(addressIndex);
  //   const allSelectedNames = documentsArray.controls
  //     .map((control, index) => {
  //       if (index !== currentDocIndex) {
  //         return control.get('docName')?.value;
  //       }
  //       return null;
  //     })
  //     .filter((name) => name !== null);

  //   return this.documentTypes.filter(
  //     (option) => !allSelectedNames.includes(option.value)
  //   );
  // }

  onDocFileSelect(event: {
    file: File;
    componentIndex: number;
    docIndex: number;
  }) {
    const { file, componentIndex, docIndex } = event;

    this.fileUploadService.convertToBase64(file).then((data) => {
      const docGroup = this.getDocumentsArray(componentIndex).at(
        docIndex
      ) as FormGroup;
      docGroup.patchValue({
        docType: data.mimeType,
        docData: data.base64,
      });
    });
  }

  addDocument(addressIndex: number) {
    const addressGroup = this.addresses.at(addressIndex) as FormGroup;
    const docsArray = addressGroup.get('addressDocuments') as FormArray;
    const newDocControl = this.userFormService.createDocumentGroup();

    newDocControl.get('docType')?.valueChanges.subscribe(() => {
      // Trigger change detection to refresh all dropdown options
      this.cdr.detectChanges();
    });
    docsArray.push(newDocControl);
  }

  removeDocument(addressIndex: number, docIndex: number) {
    const docsArray = this.addresses
      .at(addressIndex)
      .get('addressDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }
}
