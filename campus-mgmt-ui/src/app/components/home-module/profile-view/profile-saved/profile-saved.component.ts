import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import {
  DocumentDto,
  Personal,
} from '../../user-details/profile-layout/profile-form';
import { CommonModule } from '@angular/common';
import {
  FormArray,
  FormGroup,
  FormControl,
  AbstractControl,
} from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { UserApiService } from '../../../../services/profile-service/services/user-api.service';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { ApplicationDataService } from '../../../../shared/common-service/app-service';
import { AlertService } from '../../../../shared/common-service/alert-notify.service';

@Component({
  selector: 'app-profile-saved',
  imports: [CommonModule, SharedPrimeNgModule],
  templateUrl: './profile-saved.component.html',
  styleUrl: './profile-saved.component.css',
})
export class ProfileSavedComponent implements OnInit {
  profileForm!: FormGroup;
  isEditMode = false;
  profileImageUrl: string | null = null;
  addressProofUrl: string;
  familyProfileImageUrl: string | null = null;
  userDetails: any = null;

  maritalStatusOptions: any = [];

  constructor(
    private router: Router,
    private userApiService: UserApiService,
    private userFormsService: UserFormsService,
    private apiServiceData: ApplicationDataService,
    private notify: AlertService
  ) {
  }

  ngOnInit(): void {
    this.userDetails = this.apiServiceData.getProfileData();
    this.profileForm = this.userFormsService.createProfileForm(
      this.userDetails
    );
    // this.loadUserProfile(27);
  }

  loadUserProfile(userId: number): void {
    this.userApiService.getUserProfileById(userId).subscribe({
      next: (userData: Personal) => {
        (this.userDetails = userData),
          (this.profileForm =
            this.userFormsService.createProfileForm(userData));
      },
      error: (error) => {
        if (!(error as any).__interceptorHandled) {
          const errorMessage =
            error?.error?.description ||
            error?.error?.message ||
            'Failed to fetch profile!';

          this.notify.error('Profile fetch Failed', errorMessage);
        }
        console.error('Error fetching profile:', error);
      },
    });
  }

  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
    if (!this.isEditMode) {
      // this.loadUserProfile(26); // Reset form to original data
    }
  }

  saveChanges() {
    if (this.profileForm.valid) {
      this.userApiService.updateUserProfile(this.profileForm.value).subscribe({
        next: () => {
          this.isEditMode = false;
          console.log('Profile updated successfully');
        },
        error: (error) => console.error('Error updating profile:', error),
      });
    }
  }

  onProfileImageSelect(event: any) {
    const file = event.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.profileImageUrl = e.target.result;
        this.profileForm.patchValue({
          basicDetails: { profileImage: { fileUrl: this.profileImageUrl } },
        });
      };
      reader.readAsDataURL(file);
    }
  }

  onAddressProofSelect(event: any): void {
    const file: File = event.files?.[0];
    if (file) {
      const reader = new FileReader();

      reader.onload = () => {
        const dataURL = reader.result as string;
        const base64Data = dataURL.split(',')[1];

        const documentDto: DocumentDto = {
          docName: file.name,
          docType: file.type,
          docData: base64Data,
        };

        if (file.type.startsWith('image/') || file.type === 'application/pdf') {
          this.addressProofUrl = dataURL;
        }

        const proofControl = this.addresses.get('addressDocuments');
        if (proofControl) {
          proofControl.setValue([documentDto]); // Set as an array
          console.log('Address proof set in form:', [documentDto.docName]);
        } else {
          console.warn('No addressDocuments control found in form!');
        }
      };

      reader.readAsDataURL(file);
    }
  }

  onAadharCardUpload(event: any, fileName: any): void {
    const file: File = event.files?.[0];
    if (file) {
      const reader = new FileReader();

      reader.onload = () => {
        const dataURL = reader.result as string;
        const base64Data = dataURL.split(',')[1];

        const documentDto: DocumentDto = {
          docName: fileName,
          docType: file.type,
          docData: base64Data,
        };
        const personalDocsArray = this.profileForm
          .get('basicDetails')
          ?.get('personalDocuments') as FormArray;
        personalDocsArray.push(
          this.userFormsService.createDocumentGroup(documentDto)
        );
      };

      reader.readAsDataURL(file);
    }
  }

  get profileImage() {
    const docs = this.basicDetails.get('personalDocuments')?.value || [];
    return docs.find((doc: DocumentDto) => doc.docName === 'profileImage');
  }

  get aadharCard() {
    const docs = this.basicDetails.get('personalDocuments')?.value || [];
    return docs.find((doc: DocumentDto) => doc.docName === 'aadharCard');
  }

  get addressAadharCard() {
    const docs =
      this.basicDetails.get('addresses')?.get('addressDocuments')?.value || [];
    return docs.find((doc: DocumentDto) => doc.docName === 'aadharCard');
  }

  getFormControl(formGroup: AbstractControl, controlName: string): FormControl {
    return formGroup.get(controlName) as FormControl;
  }
  // Form Array Getters for preview
  get basicDetails(): FormGroup {
    return this.profileForm.get('basicDetails') as FormGroup;
  }
  get personalDocuments() {
    return this.profileForm.get('personalDocuments') as FormArray;
  }
  get families() {
    return this.profileForm.get('families') as FormArray;
  }
  get addresses() {
    return this.profileForm.get('addresses') as FormArray;
  }
  get educations() {
    return this.profileForm.get('educations') as FormArray;
  }
  get experiences() {
    return this.profileForm.get('experiences') as FormArray;
  }
  get vehicles() {
    return this.profileForm.get('vehicles') as FormArray;
  }
  get medicals() {
    return this.profileForm.get('medicals') as FormArray;
  }

  downloadDocument(doc: DocumentDto): void {
    // Create a blob from the base64 data
    const byteCharacters = atob(doc.docData);
    const byteNumbers = new Array(byteCharacters.length);

    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: doc.docType });

    // Create a temporary URL for the blob
    const url = window.URL.createObjectURL(blob);

    // Create a temporary link element and trigger the download
    const a = document.createElement('a');
    a.href = url;
    a.download = `${this.removeExtension(doc.docName)}.${this.getFileExtension(
      doc.docType
    )}`;
    document.body.appendChild(a);
    a.click();

    // Clean up
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }

  removeExtension(fileName: string): string {
    return fileName.replace(/\.[^/.]+$/, ''); // removes the last dot-extension
  }

  getFileExtension(mimeType: string): string {
    const mimeToExt: { [key: string]: string } = {
      'application/pdf': 'pdf',
      'image/png': 'png',
      'image/jpeg': 'jpg',
      'image/jpg': 'jpg',
      'application/msword': 'doc',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document':
        'docx',
      // Add more MIME types as needed
    };

    return mimeToExt[mimeType] || 'doc';
  }
}
