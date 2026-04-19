import { Component, OnInit } from '@angular/core';
import { ExperienceComponent } from '../experience/experience.component';
import { VehicleComponent } from '../vehicle/vehicle.component';
import { MedicalComponent } from '../medical/medical.component';
import { AddressComponent } from '../address/address.component';
import { EducationComponent } from '../education/education.component';
import { FamilyDetailsComponent } from '../family-details/family-details.component';
import { PersonalComponent } from '../personal/personal.component';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { MenuItem, MessageService } from 'primeng/api';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
} from '@angular/forms';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { UserApiService } from '../../../../services/profile-service/services/user-api.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApplicationDataService } from '../../../../shared/common-service/app-service';
import { AlertService } from '../../../../shared/common-service/alert-notify.service';
import { CheckboxChangeEvent } from 'primeng/checkbox';
import { DraftStorageService } from '../../../../services/profile-service/services/draft-storage.service';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormSubmissionService } from '../../../../services/form-submission.service';
import { animate, style, transition, trigger } from '@angular/animations';
import { Role, RoleService } from '../../../../services/account-service/roles.service';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../../services/account-service/auth.service';

@Component({
  selector: 'app-profile-layout',
  standalone: true,
  imports: [
    SharedPrimeNgModule,
    PersonalComponent,
    FamilyDetailsComponent,
    AddressComponent,
    EducationComponent,
    ExperienceComponent,
    VehicleComponent,
    MedicalComponent,
    CommonModule,
    RouterModule,
    SharedModule,
  ],
  templateUrl: './profile-layout.component.html',
  styleUrl: './profile-layout.component.css',
  animations: [
    trigger('stepTransition', [
      transition('* => *', [
        style({ opacity: 0, transform: 'translateX(100px)' }),
        animate(
          '300ms ease-out',
          style({ opacity: 1, transform: 'translateX(0)' })
        ),
      ]),
    ]),
  ],
})
export class ProfileLayoutComponent implements OnInit {

  personalForm: FormGroup;
  private roleSubscription: Subscription = new Subscription();
  copyPermanentToCorrespondence: boolean = false;
  sameAddressControl = new FormControl(false);
  selectedFamilyIndex: number = 0;
  formSteps: MenuItem[];
  currentStepIndex = 0;
  highestCompletedStep: number = -1;
  roleType: string = '';
  filteredRoles: Role[] = [];
  roles: Role[] = [];

  constructor(
    public userDetailsService: UserFormsService,
    private userApiService: UserApiService,
    private router: Router,
    private route: ActivatedRoute,
    private apiServiceData: ApplicationDataService,
    private notify: AlertService,
    private draftService: DraftStorageService,
    private messageService: MessageService,
    private formSubmissionService: FormSubmissionService,
    private roleService: RoleService,
    private authService: AuthService
  ) {
    // Create the form with a role FormControl
    this.personalForm = this.userDetailsService.createProfileForm(dummyData);
    // this.personalForm = new FormGroup({
    //   role: new FormControl(''),
    // });
  }

  ngOnInit() {
    // const draft = this.draftService.loadDraft();
    // if (draft) {
    //   this.personalForm.patchValue(draft);
    // }
    // Subscribe to role changes
    this.roleSubscription.add(
      this.personalForm.get('role')?.valueChanges.subscribe(roleValue => {
        if (roleValue) {
          this.roleService.setSelectedRole(roleValue);
          console.log('Selected role:', roleValue);
        }
      }) || new Subscription()
    );

    // Handle route parameters
    this.roleSubscription.add(
      this.route.params.subscribe(params => {
        if (params['type']) {
          this.roleType = params['type'];
          this.formSubmissionService.setRoleType(this.roleType);
          this.updateFilteredRoles();
          console.log("Role type from route params:", this.roleType);
        } else {
          this.roleSubscription.add(
            this.formSubmissionService.roleType$.subscribe(role => {
              if (!role) {
                this.formSubmissionService.setRoleType('student');
                this.roleType = 'student';
                this.updateFilteredRoles();
                console.log("Default role type set:", this.roleType);
              } else {
                this.roleType = role;
                this.updateFilteredRoles();
                console.log("Role type from service:", this.roleType);
              }
            })
          );
        }
      })
    );
    this.initFormSteps();
  }

  initFormSteps() {
    this.formSteps = [
      { label: 'Personal Details', command: () => this.goToStep(0) },
      { label: 'Family Details', command: () => this.goToStep(1) },
      { label: 'Address', command: () => this.goToStep(2) },
      { label: 'Education', command: () => this.goToStep(3) },
      { label: 'Experience', command: () => this.goToStep(4) },
      { label: 'Vehicle', command: () => this.goToStep(5) },
      { label: 'Medical', command: () => this.goToStep(6) },
      { label: 'Review & Submit', command: () => this.goToStep(7) },
    ];
  }

  goToStep(index: number) {
    // Option 1: Allow jumping to any step
    // this.currentStepIndex = index;

    // Option 2: Only allow going to completed steps or the next step
    if (
      this.isStepValid(this.currentStepIndex) ||
      index <= this.highestCompletedStep + 1
    ) {
      this.currentStepIndex = index;
    } else {
      this.messageService.add({
        severity: 'warn',
        summary: 'Complete Current Step',
        detail: 'Please complete the current step before proceeding.',
      });
      // Mark all controls as touched to trigger validation messages
      this.markCurrentStepAsTouched();
      // Notify the form submission service to trigger validation
      this.formSubmissionService.notifyFormSubmitted();
    }
  }

  nextStep() {
    if (this.isStepValid(this.currentStepIndex)) {
      if (this.currentStepIndex < this.formSteps.length - 1) {
        this.currentStepIndex++;
      }
    } else {
      this.messageService.add({
        severity: 'warn',
        summary: 'Complete Current Step',
        detail: 'Please complete the current step before proceeding.',
      });
      // Mark all controls as touched to trigger validation messages
      this.markCurrentStepAsTouched();
      // Notify the form submission service to trigger validation
      this.formSubmissionService.notifyFormSubmitted();
    }
  }

  prevStep() {
    if (this.currentStepIndex > 0) {
      this.currentStepIndex--;
    }
  }

  isStepValid(stepIndex: number): boolean {
    // Check validity based on the current step
    switch (stepIndex) {
      case 0: // Personal Details
        return this.basicDetails.valid && this.personalForm.get('role')?.valid === true;
      case 1: // Family Details
        return this.families.valid;
      case 2: // Address
        return this.addresses.valid;
      case 3: // Education
        return this.educations.valid;
      case 4: // Experience
        return this.experiences.valid;
      case 5: // Vehicle
        return this.vehicles.valid;
      case 6: // Medical
        return this.medicals.valid;
      default:
        return true;
    }
  }

  markCurrentStepAsTouched() {
    // Mark controls as touched based on the current step
    switch (this.currentStepIndex) {
      case 0: // Personal Details
        this.markFormGroupAsTouched(this.basicDetails);
        this.personalForm.get('role')?.markAsTouched();
        break;
      case 1: // Family Details
        this.markFormArrayAsTouched(this.families);
        break;
      case 2: // Address
        this.markFormArrayAsTouched(this.addresses);
        break;
      case 3: // Education
        this.markFormArrayAsTouched(this.educations);
        break;
      case 4: // Experience
        this.markFormArrayAsTouched(this.experiences);
        break;
      case 5: // Vehicle
        this.markFormArrayAsTouched(this.vehicles);
        break;
      case 6: // Medical
        this.markFormArrayAsTouched(this.medicals);
        break;
    }
  }

  markFormGroupAsTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((key) => {
      const control = formGroup.get(key);
      control?.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupAsTouched(control);
      } else if (control instanceof FormArray) {
        this.markFormArrayAsTouched(control);
      }
    });
  }

  markFormArrayAsTouched(formArray: FormArray) {
    formArray.controls.forEach((control) => {
      if (control instanceof FormGroup) {
        this.markFormGroupAsTouched(control);
      } else {
        control.markAsTouched();
      }
    });
  }

  updateFilteredRoles() {
    // Get the appropriate roles from the service
    this.filteredRoles = this.roleService.getRolesByType(this.roleType);
    this.roles = [...this.filteredRoles]; // Update the roles property for the template

    // Reset the form control when role type changes
    this.personalForm.get('role')?.reset('');
  }

  ngOnDestroy() {
    this.roleSubscription.unsubscribe();
  }

  saveDraft(): void {
    const formValue = this.getFormValueWithDisabledControls();

    const payload = {
      role: formValue.role,
      ...formValue.basicDetails, // spread the basic details to root
      personalDocuments: formValue.basicDetails.personalDocuments || [],
      families: formValue.families || [],
      addresses: formValue.addresses || [],
      educations: formValue.educations || [],
      experiences: formValue.experiences || [],
      vehicles: formValue.vehicles || [],
      medicals: formValue.medicals || [],
    }; // gets disabled fields too
    this.draftService.saveDraft(payload);
    this.messageService.add({ severity: 'info', summary: 'Draft Saved' });
  }

  submitForm() {
    if (this.personalForm.valid) {
      // Get all form data, including disabled controls
      const formValue = this.getFormValueWithDisabledControls();

      const basicDetailsWithStatus = {
        ...formValue.basicDetails,
        profileStatus: 'Submit'
      };
      const payload = {
        role: Array.isArray(formValue.role) ? formValue.role : [formValue.role],
        ...basicDetailsWithStatus, // spread with added personalStatus
        personalDocuments: basicDetailsWithStatus.personalDocuments || [],
        families: formValue.families || [],
        addresses: formValue.addresses || [],
        educations: formValue.educations || [],
        experiences: formValue.experiences || [],
        vehicles: formValue.vehicles || [],
        medicals: formValue.medicals || [],
      };

      this.authService.createUserProfile(payload).subscribe({
        next: (savedData) => {
          this.notify.success('Success', 'Profile created successfully');
          this.apiServiceData.setProfileData(savedData);
          this.router.navigate(['/dashboard/profile-saved']);
        },
        error: (error) => {
          if (!(error as any).__interceptorHandled) {
            const errorMessage =
              error?.error?.description ||
              error?.error?.message ||
              'Failed to save profile!';

            this.notify.error('Profile Creation Failed', errorMessage);
          }
          console.error('Error creating profile:', error);
        },
      });
    } else {
      this.markFormGroupTouched(this.personalForm);
      this.formSubmissionService.notifyFormSubmitted();
      this.notify.error('Validation Error', 'Please fill all required fields');
    }
  }

  markFormGroupTouched(formGroup: FormGroup | FormArray) {
    Object.values(formGroup.controls).forEach((control) => {
      control.markAsTouched();

      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      }
    });
  }
  // Helper method to get form values including disabled controls
  getFormValueWithDisabledControls() {
    // Create a deep copy of the form value
    const formValue = { ...this.personalForm.getRawValue() };

    // Process address values to ensure disabled controls are included
    if (formValue.addresses && Array.isArray(formValue.addresses)) {
      formValue.addresses = formValue.addresses.map(
        (address: { addressType: any }) => {
          // Make sure each address has the correct addressType
          return {
            ...address,
            addressType: address.addressType,
          };
        }
      );
    }

    return formValue;
  }

  onCheckboxToggle(event: CheckboxChangeEvent): void {
    // Extract the boolean value from the CheckboxChangeEvent
    this.copyPermanentToCorrespondence = event.checked;

    if (event.checked) {
      // Use getRawValue to get values including disabled fields
      const permanentValue = { ...this.permanentAddress.getRawValue() };
      permanentValue.addressType = 'Correspondence';
      this.correspondenceAddress.patchValue(permanentValue);

      // Disable the correspondence address, but keep addressType
      const addressTypeControl = this.correspondenceAddress.get('addressType');
      const addressTypeValue = addressTypeControl?.value;
      this.correspondenceAddress.disable();

      // Ensure addressType is still properly set
      if (addressTypeControl) {
        addressTypeControl.setValue('Correspondence');
      }
    } else {
      // Save the address type before resetting
      const addressType =
        this.correspondenceAddress.get('addressType')?.value ||
        'Correspondence';

      // Reset and enable the form
      this.correspondenceAddress.reset({ addressType });
      this.correspondenceAddress.enable();

      // Re-disable the addressType field if needed
      const addressTypeControl = this.correspondenceAddress.get('addressType');
      if (addressTypeControl) {
        addressTypeControl.setValue('Correspondence');
        addressTypeControl.disable();
      }
    }
  }

  get controls() {
    return this.personalForm.controls;
  }

  get basicDetails(): FormGroup {
    return this.personalForm.get('basicDetails') as FormGroup;
  }

  get families(): FormArray {
    return this.personalForm.get('families') as FormArray;
  }

  get addresses(): FormArray {
    return this.personalForm.get('addresses') as FormArray;
  }

  get educations(): FormArray {
    return this.personalForm.get('educations') as FormArray;
  }

  get experiences(): FormArray {
    return this.personalForm.get('experiences') as FormArray;
  }

  get vehicles(): FormArray {
    return this.personalForm.get('vehicles') as FormArray;
  }

  get medicals(): FormArray {
    return this.personalForm.get('medicals') as FormArray;
  }

  get permanentAddress(): FormGroup {
    return this.addresses.at(0) as FormGroup;
  }

  get correspondenceAddress(): FormGroup {
    return this.addresses.at(1) as FormGroup;
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }
}

const dummyData = {
  firstName: 'John',
  middleName: 'A.',
  lastName: 'Doe',
  gander: 'Male',
  caste: 'General',
  community: 'Christian',
  dob: '1990-01-01',
  email: 'john.doe@example.com',
  mobile: '9876543210',
  alternateMobile: '9123456789',
  alternateEmail: 'john.alt@example.com',
  maritalStatus: 'Married',

  families: [
    {
      memberName: 'Jane Doe',
      memberRelation: 'Wife',
      memberGender: 'Female',
      memberDob: '1992-02-02',
      memberOccupation: 'Teacher',
      memberOrg: 'ABC School',
      memberStatus: 'Alive',
      memberEmail: 'jane.doe@example.com',
      memberMobile: '9876543211',
    },
  ],

  addresses: [
    {
      addressLine1: '123 Main St',
      addressLine2: 'Apt 4B',
      unitNumber: '4B',
      unitName: 'Sunset Towers',
      locality: 'Downtown',
      city: 'Metropolis',
      district: 'Metro District',
      state: 'StateX',
      postalCode: '123456',
      countryCode: 'IN',
      countryName: 'India',
      latitude: '28.7041',
      longitude: '77.1025',
      region: 'North',
      landmark: 'Near Park',
      addressType: 'Permanent',
      status: 'Active',
    },
    {
      addressLine1: '456 Office Blvd',
      addressLine2: 'Suite 200',
      unitNumber: '200',
      unitName: 'Business Center',
      locality: 'Financial District',
      city: 'Metropolis',
      district: 'Business District',
      state: 'StateX',
      postalCode: '123789',
      countryCode: 'IN',
      countryName: 'India',
      latitude: '28.6139',
      longitude: '77.2090',
      region: 'Central',
      landmark: 'Near Central Bank',
      addressType: 'Correspondence',
      status: 'Active',
    },
  ],

  educations: [
    {
      courseName: 'B.Tech',
      courseSubject: 'Computer Science',
      collegeName: 'ABC Engineering College',
      collegeCity: 'Techville',
      collegeState: 'StateY',
      universityName: 'XYZ University',
      boardName: 'Tech Board',
      startDate: '2008-06-01',
      endDate: '2012-05-31',
      obtainMarks: '820',
      maxMarks: '1000',
      status: 'Completed',
      memberDocuments: [],
    },
  ],

  experiences: [
    {
      institutionName: 'TechCorp',
      designation: 'Senior Developer',
      skills: 'Java, Angular, Spring',
      joiningDate: '2013-07-01',
      leavingDate: '2020-12-31',
      leavingReason: 'Career Growth',
      remark: 'Excellent Performance',
      status: 'Relieved',
      memberDocuments: [],
    },
  ],

  vehicles: [
    {
      registrationNumber: 'MH12AB1234',
      registrationDate: '2018-01-01',
      chassisNumber: 'CH123456789',
      engineNumber: 'EN987654321',
      brandName: 'Maruti',
      vehicleType: 'Car',
      fuelType: 'Petrol',
      dlNumber: 'DL1234567',
      dlExpireDate: '2028-01-01',
      isInsured: true,
      insuranceId: 'INS123456',
      insuranceCompanyName: 'ICICI Lombard',
      insuranceType: 'Comprehensive',
      insuranceExpireDate: '2025-01-01',
      isPucCertificated: true,
      pucCertificateId: 'PUC123456',
      pucExpireDate: '2025-01-01',
      remark: 'No Issues',
      memberDocuments: [],
    },
  ],

  medicals: [
    {
      bloodGroup: 'O+',
      medicalHistory: 'None',
      diseases: 'None',
      medications: 'None',
      symptoms: 'None',
      vitals: 'Healthy',
      diagnoses: 'None',
      treatments: 'None',
      therapies: 'None',
      psychosocial: 'None',
      memberDocuments: [],
    },
  ],
};