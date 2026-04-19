import { ChangeDetectionStrategy, ChangeDetectorRef, Component, SimpleChanges } from '@angular/core';
import { AuthService } from '../../../services/account-service/auth.service';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { Observable } from 'rxjs';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { UserFormsService } from '../../../services/profile-service/services/user-details-form-service';
import { UserApiService } from '../../../services/profile-service/services/user-api.service';
import { FormPatchService } from '../../../services/profile-service/form-patch-service.service';

type SectionMode = 'view' | 'edit';

interface EditState {
  basicDetails: boolean;
  families: boolean;
  addresses: boolean;
  educations: boolean;
  experiences: boolean;
  vehicles: boolean;
  medicals: boolean;
}

type SectionModeState = {
  [K in keyof EditState]: SectionMode | SectionMode[];
};

@Component({
  selector: 'app-account-details',
  imports: [SharedPrimeNgModule],
  standalone: true,
  templateUrl: './account-details.component.html',
  styleUrl: './account-details.component.css',
})
export class AccountDetailsComponent {
  accountDetails$: Observable<any>;
  accountForm!: FormGroup;
  family: FormGroup;
  sameAddressControl = new FormControl(false);
  currentUserId: string;
  sectionModes: SectionModeState = {
    basicDetails: 'view',
    families: [],
    addresses: [],
    educations: [],
    experiences: [],
    vehicles: [],
    medicals: [],
  };

  // Options arrays
  insuranceTypeOptions = ['Health', 'Life', 'Vehicle', 'Property'];
  bloodGroupOptions = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
  fuelTypeOptions = ['Petrol', 'Diesel', 'CNG', 'Electric', 'Hybrid'];
  vehicleTypeOptions = ['Car', 'Bike', 'Truck', 'Bus', 'Auto'];
  relationOptions = ['Father', 'Mother', 'Spouse', 'Child', 'Sibling', 'Wife', 'Other'];
  addressTypeOptions = ['Permanent', 'Correspondence', 'Office', 'Temporary'];
  genderOptions = ['Male', 'Female', 'Other'];
  statusOptions = ['Active', 'Inactive', 'Alive'];
  expStatusOptions = ['Working', 'Relieved'];
  bloodGroups = ['A+', 'O+', 'AB-'];
  maritalStatusOptions = ['Married', 'Single', 'Divorced'];
  educationStatusOptions = ['Completed', 'Pursuing'];
  booleanOptions = ['Yes', 'No'];
  casteOptions = ['General', 'OBC', 'SC', 'ST'];
  communityOptions = ['Hindu', 'Muslim', 'Christian', 'Sikh'];
  // Selected tab indices
  selectedFamilyIndex: number = 0;
  selectedAddressIndex: number = 0;
  selectedEducationIndex: number = 0;
  selectedExperienceIndex: number = 0;
  selectedVehicleIndex: number = 0;
  selectedMedicalIndex: number = 0;
  noUserDetailsFound = false;

  originalData: { [key in keyof EditState]: any } = {
    basicDetails: {},
    families: [],
    addresses: [],
    educations: [],
    experiences: [],
    vehicles: [],
    medicals: [],
  };

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    public userDetailsService: UserFormsService,
    private userApiService: UserApiService,
    private formPatchService: FormPatchService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.noUserDetailsFound = false;
    this.authService.userInfo$.subscribe((user) => {
      // Handle user info subscription if needed
    });

    this.selectedFamilyIndex = 0;
    const userInfo = this.authService.getEnhancedUser();
    console.log('Complete user details:', userInfo?.additionalDetails);
    if (!userInfo || !userInfo?.additionalDetails?.userId) {
      console.warn('User details not found. Skipping form creation.');
      this.noUserDetailsFound = true;
      return;
    }
    this.currentUserId = userInfo?.additionalDetails?.userId;
    this.accountForm = this.userDetailsService.createProfileForm(userInfo?.additionalDetails);

    // Subscribe to enhanced user info changes
    this.authService.enhancedUserInfo$.subscribe(userInfo => {
      console.log('Updated user details:', userInfo?.additionalDetails);
    });

    // Initialize original data and section modes
    this.initializeOriginalData();
    this.initializeSectionModes();
  }

  // ngOnChanges(changes: SimpleChanges): void {
  //   if (changes['families'] && changes['families'].currentValue) {
  //     this.patchFormValues();
  //   }
  // }

  private patchFormValues(): void {
    if (!this.families) return;

    this.formPatchService.patchFormValues(this.families, {
      dateFields: ['memberDob'],
      selectFields: {
        memberRelation: this.relationOptions,
        memberStatus: this.statusOptions,
      },
      radioFields: {
        memberGender: {
          male: 'Male',
          female: 'Female',
        },
      },
    });

    // Trigger change detection to ensure UI updates
    this.cdr.detectChanges();
  }

  private initializeOriginalData(): void {
    this.originalData.basicDetails = { ...this.accountForm.get('basicDetails')?.value };
    this.originalData.families = this.families.controls.map(control => ({ ...control.value }));
    this.originalData.addresses = this.addresses.controls.map(control => ({ ...control.value }));
    this.originalData.educations = this.educations.controls.map(control => ({ ...control.value }));
    this.originalData.experiences = this.experiences.controls.map(control => ({ ...control.value }));
    this.originalData.vehicles = this.vehicles.controls.map(control => ({ ...control.value }));
    this.originalData.medicals = this.medicals.controls.map(control => ({ ...control.value }));
  }

  private initializeSectionModes(): void {
    this.sectionModes.families = this.families.controls.map(() => 'view');
    this.sectionModes.addresses = this.addresses.controls.map(() => 'view');
    this.sectionModes.educations = this.educations.controls.map(() => 'view');
    this.sectionModes.experiences = this.experiences.controls.map(() => 'view');
    this.sectionModes.vehicles = this.vehicles.controls.map(() => 'view');
    this.sectionModes.medicals = this.medicals.controls.map(() => 'view');
  }

  // Utility methods
  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  getFormControl(control: AbstractControl | null): FormControl {
    return control as FormControl;
  }

  getSectionMode(section: keyof EditState, index?: number): 'view' | 'edit' {
    const mode = this.sectionModes[section];
    if (Array.isArray(mode) && index !== undefined) {
      return mode[index];
    }
    return mode as 'view' | 'edit';
  }

  // Generic edit/cancel methods
  onEdit(section: keyof EditState, index?: number) {
    if (index !== undefined && Array.isArray(this.sectionModes[section])) {
      (this.sectionModes[section] as Array<'view' | 'edit'>)[index] = 'edit';
    } else {
      this.sectionModes[section] = 'edit';
      setTimeout(() => {
        this.patchBasicDetails();
      });
    }
  }

  onCancel(section: keyof EditState, index?: number) {
    if (index !== undefined && Array.isArray(this.sectionModes[section])) {
      (this.sectionModes[section] as Array<'view' | 'edit'>)[index] = 'view';
      this.restoreSectionData(section, index);
    } else {
      this.sectionModes[section] = 'view';
      this.restoreSectionData(section);
    }
  }

  restoreSectionData(section: keyof EditState, index?: number): void {
    const control = this.accountForm.get(section);
    if (Array.isArray(this.originalData[section]) && index !== undefined) {
      (control as FormArray).at(index).patchValue(this.originalData[section][index]);
    } else {
      control?.patchValue(this.originalData[section]);
    }
  }

  // Basic Details methods
  saveBasicDetails(): void {
    const updatedBasicDetails = this.basicDetails.value;

    this.userApiService.updateBasicSection({ basicDetails: updatedBasicDetails }, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Basic details updated successfully',
        });
        this.sectionModes.basicDetails = 'view';
        this.originalData.basicDetails = { ...updatedBasicDetails };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update basic details',
        });
      }
    });
  }

  // Family methods
  addFamily() {
    const group = this.fb.group({
      memberRelation: [''],
      memberName: [''],
      memberGender: [''],
      memberDob: [null],
      memberOccupation: [''],
      memberOrg: [''],
      memberStatus: [''],
      memberEmail: [''],
      memberMobile: ['']
      // Add any other fields that are referenced in your HTML template
    });

    this.families.push(group);
    (this.sectionModes.families as string[]).push('edit');
    this.selectedFamilyIndex = this.families.length - 1;
  }

  saveFamily(index: number): void {
    const updated = this.families.at(index).value;

    this.userApiService.updateFamilySection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Family member updated',
        });
        (this.sectionModes.families as SectionMode[])[index] = 'view';
        this.originalData.families[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Unknown error while updating family member',
        });
      }
    });
  }

  cancelFamily(index: number): void {
    (this.sectionModes.families as SectionMode[])[index] = 'view';
    this.families.at(index).patchValue(this.originalData.families[index]);
  }

  confirmDelete(index: number) {
    this.families.removeAt(index);
    (this.sectionModes.families as string[]).splice(index, 1);
    this.selectedFamilyIndex = Math.max(0, this.families.length - 1);
  }

  // Address methods
  addAddress() {
    const group = this.fb.group({
      addressType: [''],
      address: [''],
      city: [''],
      state: [''],
      pincode: [''],
      country: ['']
    });

    this.addresses.push(group);
    (this.sectionModes.addresses as string[]).push('edit');
    this.selectedAddressIndex = this.addresses.length - 1;
  }

  saveAddress(index: number): void {
    const updated = this.addresses.at(index).value;

    this.userApiService.updateAddressSection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Address updated successfully',
        });
        (this.sectionModes.addresses as SectionMode[])[index] = 'view';
        this.originalData.addresses[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update address',
        });
      }
    });
  }

  cancelAddress(index: number): void {
    (this.sectionModes.addresses as SectionMode[])[index] = 'view';
    this.addresses.at(index).patchValue(this.originalData.addresses[index]);
  }

  removeAddress(index: number) {
    this.addresses.removeAt(index);
    (this.sectionModes.addresses as string[]).splice(index, 1);
    this.selectedAddressIndex = Math.max(0, this.addresses.length - 1);
  }

  // Education methods
  addEducation() {
    const group = this.fb.group({
      courseName: [''],
      courseSubject: [''],
      collegeName: [''],
      collegeCity: [''],
      collegeState: [''],
      universityName: [''],
      boardName: [''],
      startDate: [null],
      endDate: [null],
      obtainMarks: [''],
      maxMarks: [''],
      status: ['']
    });

    this.educations.push(group);
    (this.sectionModes.educations as string[]).push('edit');
    this.selectedEducationIndex = this.educations.length - 1;
  }

  saveEducation(index: number): void {
    const updated = this.educations.at(index).value;

    this.userApiService.updateEducationSection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Education details updated successfully',
        });
        (this.sectionModes.educations as SectionMode[])[index] = 'view';
        this.originalData.educations[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update education details',
        });
      }
    });
  }

  cancelEducation(index: number): void {
    (this.sectionModes.educations as SectionMode[])[index] = 'view';
    this.educations.at(index).patchValue(this.originalData.educations[index]);
  }

  removeEducation(index: number) {
    this.educations.removeAt(index);
    (this.sectionModes.educations as string[]).splice(index, 1);
    this.selectedEducationIndex = Math.max(0, this.educations.length - 1);
  }

  // Experience methods
  addExperience() {
    const group = this.fb.group({
      institutionName: [''],    // Changed from 'company'
      designation: [''],        // Changed from 'position'
      skills: [''],            // New field
      joiningDate: [null],     // Changed from 'fromDate'
      leavingDate: [null],     // Changed from 'toDate'
      leavingReason: [''],     // New field
      status: [''],            // New field
      remark: ['']             // Changed from 'description'
    });

    this.experiences.push(group);
    (this.sectionModes.experiences as string[]).push('edit');
    this.selectedExperienceIndex = this.experiences.length - 1;
  }

  saveExperience(index: number): void {
    const updated = this.experiences.at(index).value;

    this.userApiService.updateExperienceSection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Experience details updated successfully',
        });
        (this.sectionModes.experiences as SectionMode[])[index] = 'view';
        this.originalData.experiences[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update experience details',
        });
      }
    });
  }

  cancelExperience(index: number): void {
    (this.sectionModes.experiences as SectionMode[])[index] = 'view';
    this.experiences.at(index).patchValue(this.originalData.experiences[index]);
  }

  removeExperience(index: number) {
    this.experiences.removeAt(index);
    (this.sectionModes.experiences as string[]).splice(index, 1);
    this.selectedExperienceIndex = Math.max(0, this.experiences.length - 1);
  }

  // Vehicle methods
  addVehicle() {
    const group = this.fb.group({
      // Basic Vehicle Information
      registrationNumber: [''],     // Changed from 'vehicleNumber'
      registrationDate: [null],     // New field
      chassisNumber: [''],          // New field
      engineNumber: [''],           // New field
      brandName: [''],              // Changed from 'makeModel'
      vehicleType: [''],            // Kept same
      fuelType: [''],               // Kept same

      // Driving License Details
      dlNumber: [''],               // New field
      dlExpireDate: [null],         // New field

      // Insurance Details
      isInsured: [''],              // New field
      insuranceId: [''],            // Changed from 'insuranceNumber'
      insuranceCompanyName: [''],   // New field
      insuranceType: [''],          // New field
      insuranceExpireDate: [null],  // New field

      // PUC Certificate Details
      isPucCertificated: [''],      // New field
      pucCertificateId: [''],       // New field
      pucExpireDate: [null],        // New field

      // Additional
      remark: ['']                  // New field
    });

    this.vehicles.push(group);
    (this.sectionModes.vehicles as string[]).push('edit');
    this.selectedVehicleIndex = this.vehicles.length - 1;
  }

  saveVehicle(index: number): void {
    const updated = this.vehicles.at(index).value;

    this.userApiService.updateVehicleSection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Vehicle details updated successfully',
        });
        (this.sectionModes.vehicles as SectionMode[])[index] = 'view';
        this.originalData.vehicles[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update vehicle details',
        });
      }
    });
  }

  cancelVehicle(index: number): void {
    (this.sectionModes.vehicles as SectionMode[])[index] = 'view';
    this.vehicles.at(index).patchValue(this.originalData.vehicles[index]);
  }

  removeVehicle(index: number) {
    this.vehicles.removeAt(index);
    (this.sectionModes.vehicles as string[]).splice(index, 1);
    this.selectedVehicleIndex = Math.max(0, this.vehicles.length - 1);
  }

  // Medical methods
  addMedical() {
    const group = this.fb.group({
      bloodGroup: [''],
      height: [''],
      weight: [''],
      emergencyContact: [''],
      medicalConditions: [''],
      medicalHistory: [''],    // Add missing field
      diseases: [''],          // Add missing field
      medications: [''],       // Add missing field
      symptoms: [''],          // Add missing field
      vitals: [''],           // Add missing field
      diagnoses: [''],        // Add missing field
      treatments: [''],       // Add missing field
      therapies: [''],        // Add missing field
      psychosocial: ['']      // Add missing field
    });

    this.medicals.push(group);
    (this.sectionModes.medicals as string[]).push('edit');
    this.selectedMedicalIndex = this.medicals.length - 1;
  }

  saveMedical(index: number): void {
    const updated = this.medicals.at(index).value;

    this.userApiService.updateMedicalSection(updated, this.currentUserId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Medical details updated successfully',
        });
        (this.sectionModes.medicals as SectionMode[])[index] = 'view';
        this.originalData.medicals[index] = { ...updated };
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: err?.message || 'Failed to update medical details',
        });
      }
    });
  }

  cancelMedical(index: number): void {
    (this.sectionModes.medicals as SectionMode[])[index] = 'view';
    this.medicals.at(index).patchValue(this.originalData.medicals[index]);
  }

  removeMedical(index: number) {
    this.medicals.removeAt(index);
    (this.sectionModes.medicals as string[]).splice(index, 1);
    this.selectedMedicalIndex = Math.max(0, this.medicals.length - 1);
  }

  // Form getters
  get basicDetails(): FormGroup {
    return this.accountForm.get('basicDetails') as FormGroup;
  }

  get families(): FormArray {
    return this.accountForm?.get('families') as FormArray;
  }

  get addresses(): FormArray {
    return this.accountForm?.get('addresses') as FormArray;
  }

  get educations(): FormArray {
    return this.accountForm.get('educations') as FormArray;
  }

  get experiences(): FormArray {
    return this.accountForm.get('experiences') as FormArray;
  }

  get vehicles(): FormArray {
    return this.accountForm.get('vehicles') as FormArray;
  }

  get medicals(): FormArray {
    return this.accountForm.get('medicals') as FormArray;
  }

  patchBasicDetails() {
    this.formPatchService.patchFormValues(this.basicDetails, {
      dateFields: ['dob'],
      selectFields: {
        caste: this.casteOptions,
        community: this.communityOptions,
        maritalStatus: this.maritalStatusOptions,
      },
      radioFields: {
        gender: {
          male: 'Male',
          female: 'Female',
        },
      },
    });

    // Trigger change detection to ensure UI updates
    this.cdr.detectChanges();
  }

  editFamilyTab(index: number): void {
    (this.sectionModes.families as SectionMode[])[index] = 'edit';
    setTimeout(() => {
      this.patchFormValues();
    });
  }

  editAddressTab(index: number): void {
    (this.sectionModes.addresses as SectionMode[])[index] = 'edit';
  }

  editEducationTab(index: number): void {
    (this.sectionModes.educations as SectionMode[])[index] = 'edit';
    setTimeout(() => {
      this.patchEducationValues();
    });
  }

  private patchEducationValues(): void {
    if (!this.educations) return;

    this.formPatchService.patchFormValues(this.educations, {
      dateFields: ['startDate', 'endDate'],
      selectFields: {
        // 'docIndex': this.documentOptions,
        'status': this.statusOptions
      }
    });
    this.cdr.detectChanges();
  }

  editExperienceTab(index: number): void {
    (this.sectionModes.experiences as SectionMode[])[index] = 'edit';
    setTimeout(() => {
      this.patchExperienceValues();
    });
  }

  private patchExperienceValues(): void {
    if (!this.experiences) return;

    this.formPatchService.patchFormValues(this.experiences, {
      dateFields: ['joiningDate', 'leavingDate'],
      selectFields: {
        // 'docIndex': this.documentOptions,
        status: this.expStatusOptions
      }
    });
    this.cdr.detectChanges();
  }

  editVehicleTab(index: number): void {
    (this.sectionModes.vehicles as SectionMode[])[index] = 'edit';
    setTimeout(() => {
      this.patchVehicleValues();
    });
  }

  private patchVehicleValues(): void {
    if (!this.vehicles) return;

    this.formPatchService.patchFormValues(this.vehicles, {
      dateFields: ['registrationDate', 'dlExpireDate', 'pucExpireDate', 'insuranceExpireDate'],
      selectFields: {
        vehicleType: this.vehicleTypeOptions,
        fuelType: this.fuelTypeOptions,
        // docIndex: this.documentOptions
      },
      checkboxConfig: {
        // Single true/false checkboxes
        singleCheckboxes: [
          'isInsured',
          'isPucCertificated'
        ],
        // // Multi-select checkboxes where value is an array
        // multiCheckboxes: {
        //   'preferredDays': ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
        //   'selectedSkills': this.skillOptions.map(option => option.value)
        // }
      }
    });
    this.cdr.detectChanges();
  }

  editMedicalTab(index: number): void {
    (this.sectionModes.medicals as SectionMode[])[index] = 'edit';
  }

  onInsuranceStatusChange(event: any, index: any) { }
  onPucStatusChange(event: any, index: any) { }
  hasAnyMedicalData(medicalForm: AbstractControl): boolean {
    const form = medicalForm as FormGroup;
    const fields = ['bloodGroup', 'medicalHistory', 'diseases', 'medications', 'symptoms', 'vitals', 'diagnoses', 'treatments', 'therapies', 'psychosocial'];

    return fields.some(field => {
      const value = form.get(field)?.value;
      return value && value !== 'None' && value.trim() !== '';
    })
  }
  onSubmit(): void {
    if (this.accountForm.valid) {
      this.authService.updateAccount(this.accountForm.value).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Updated',
            detail: 'Account updated successfully',
          });
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to update account',
          });
        },
      });
    }
  }
}