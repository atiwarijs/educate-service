import { Component, Input, OnInit, Type } from '@angular/core';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';
import { RouterModule } from '@angular/router';
import { UserApiService } from '../../../services/profile-service/services/user-api.service';
import { Personal } from '../user-details/profile-layout/profile-form';
import { UserDetailPopupComponent } from '../user-detail-popup/user-detail-popup.component';
import { UserFormsService } from '../../../services/profile-service/services/user-details-form-service';
import { FormArray, FormGroup } from '@angular/forms';
import { FamilyDetailsComponent } from '../user-details/family-details/family-details.component';
import { EducationComponent } from '../user-details/education/education.component';
import { ExperienceComponent } from '../user-details/experience/experience.component';
import { AddressComponent } from '../user-details/address/address.component';
import { VehicleComponent } from '../user-details/vehicle/vehicle.component';
import { MedicalComponent } from '../user-details/medical/medical.component';
import { MessageService } from 'primeng/api';
import { AlertService } from '../../../shared/common-service/alert-notify.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SharedPrimeNgModule, RouterModule, UserDetailPopupComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  displayPopup: boolean = false;
  personalForm: FormGroup;
  showArrayPopup: boolean = false;
  selectedUser: any;
  @Input() section: string;
  arrayPopupTitle: string = '';
  showFamilyPopup = false;
  dynamicComponent!: Type<any>;
  dynamicFormArray!: FormArray;
  noDataFound: boolean = false;

  sortField: string = 'firstName';
  sortOrder: number = 1; // 1 = ascending, -1 = descending

  userDetails: any = [];

  constructor(
    private messageService: MessageService,
    private userApiService: UserApiService,
    private UserFormService: UserFormsService,
    private notify: AlertService
  ) { }

  ngOnInit(): void {
    this.loadAllUsersProfile();
    // this.userDetails = this.userDetails.map((user: { dob: string; }) => ({
    //   ...user,
    //   age: this.calculateAge(user.dob)
    // }));
  }

  loadAllUsersProfile(): void {
    this.userApiService.getAllUserProfile().subscribe({
      next: (userData: Personal[] | null) => {
        const filtered = (userData || []).filter(user =>
          user && (
            user.firstName?.trim() ||
            user.lastName?.trim() ||
            user.dob // You can expand this condition
          )
        );

        if (filtered.length === 0) {
          this.userDetails = [];
          this.noDataFound = true;
          return;
        }

        this.userDetails = filtered.map((user) => ({
          ...user,
          age: user.dob ? this.calculateAge(user.dob) : undefined
        }));
        this.noDataFound = false;
      },
      error: (error) => {
        // If the error was already handled by the interceptor, don't show another alert
        if (!(error as any).__interceptorHandled) {
          const errorMessage =
            error?.error?.description ||
            error?.error?.message ||
            'Error Retrieving Profiles!';

          this.notify.error('Profile Retrieve Failed', errorMessage);
        }
        // console.error('Error retrieving profiles:', error);
      },
    });
  }

  openUserDetails(
    userIndex: number,
    section:
      | 'family'
      | 'address'
      | 'vehicle'
      | 'medical'
      | 'experience'
      | 'education'
  ) {
    const selected = this.userDetails[userIndex];

    // First, close any open popup to prevent overlapping change detection cycles
    this.showFamilyPopup = false;

    // Create the profile form and get references
    const profileForm = this.UserFormService.createProfileForm(selected);
    this.section = section;

    // Set up the component and form array first
    switch (section) {
      case 'family':
        this.dynamicComponent = FamilyDetailsComponent;

        this.dynamicFormArray = profileForm.get('families') as FormArray;
        if (!this.dynamicFormArray || this.dynamicFormArray.length === 0) {
          // Initialize with at least one empty family form
          this.dynamicFormArray =
            this.UserFormService.createEmptyFormArrayFor('family');
          // You might need to add this form array to the profileForm if needed
          profileForm.setControl('families', this.dynamicFormArray);
        }
        break;
      case 'education':
        this.dynamicComponent = EducationComponent;
        this.dynamicFormArray = profileForm.get('educations') as FormArray;
        break;
      case 'experience':
        this.dynamicComponent = ExperienceComponent;
        this.dynamicFormArray = profileForm.get('experiences') as FormArray;
        break;
      case 'address':
        this.dynamicComponent = AddressComponent;
        this.dynamicFormArray = profileForm.get('addresses') as FormArray;
        break;
      case 'vehicle':
        this.dynamicComponent = VehicleComponent;
        this.dynamicFormArray = profileForm.get('vehicles') as FormArray;
        break;
      case 'medical':
        this.dynamicComponent = MedicalComponent;
        this.dynamicFormArray = profileForm.get('medicals') as FormArray;
        break;
    }

    // Ensure the FormArray exists
    if (!this.dynamicFormArray || this.dynamicFormArray.length === 0) {
      console.warn(`${section} array is empty or not defined`);
      // Create a properly structured FormArray based on the section type
      // Instead of using createProfileForm which might cause issues
      this.dynamicFormArray =
        this.UserFormService.createEmptyFormArrayFor(section);
    }

    // Use setTimeout to break the change detection cycle
    setTimeout(() => {
      // Now open the popup in the next detection cycle
      this.showFamilyPopup = true;
    });
  }

  onFamilySave(updated: FormArray) {
    console.log('Updated family:', updated.value);
  }

  calculateAge(dob: string | Date): number {
    const birth = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const m = today.getMonth() - birth.getMonth();

    if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) {
      age--;
    }

    return age;
  }
}
