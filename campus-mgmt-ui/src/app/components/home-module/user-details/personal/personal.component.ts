import {
  ChangeDetectorRef,
  Component,
  Input,
  SimpleChanges,
} from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { CommonModule } from '@angular/common';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';
import { AuthService } from '../../../../services/account-service/auth.service';
import { debounceTime, distinctUntilChanged, filter, Subject, takeUntil } from 'rxjs';
import { UserApiService } from '../../../../services/profile-service/services/user-api.service';

@Component({
  selector: 'app-personal',
  imports: [
    SharedPrimeNgModule,
    CommonModule,
    FileUploadComponent,
    SharedModule,
  ],
  standalone: true,
  templateUrl: './personal.component.html',
  styleUrl: './personal.component.css',
})
export class PersonalComponent {
  @Input() basicDetailsForm!: FormGroup;
  private destroy$ = new Subject<void>();

  profileImageUrl: string = '';

  casteOptions = ['General', 'OBC', 'SC', 'ST'];
  communityOptions = ['Hindu', 'Muslim', 'Christian', 'Sikh'];
  maritalStatusOptions = ['Single', 'Married', 'Divorced', 'Widowed'];
  emailNotFound = false;
  statusOptions = ['Active', 'Inactive'];
  documentOptions = [
    { label: 'Aadhar', value: 'Aadhar' },
    { label: 'Profile Image', value: 'Profile' },
    { label: 'PAN Card', value: 'PAN' },
    { label: 'Others', value: 'Others' },
  ];

  constructor(
    private userFormService: UserFormsService,
    private fileUploadService: FileUploadService,
    private formPatchService: FormPatchService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService,
    private userApiService: UserApiService
  ) { }

  ngOnInit(): void {
    if (!this.basicDetailsForm) return;
    this.patchFormValues();
    this.authService.userInfo$
      .pipe(takeUntil(this.destroy$))
      .subscribe((user) => {
        setTimeout(() => {
          if (this.basicDetailsForm) {
            const nameParts = user?.['given_name']?.trim().split(' ') || [];
            this.basicDetailsForm.patchValue({
              firstName: nameParts[0] || '',
              lastName: user?.['family_name'] || '',
              email: user?.['email'] || '',
              username: user?.['preferred_username'] || ''
            });
          }
        });
      });

    this.basicDetailsForm.get('email')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        filter(() => this.basicDetailsForm.get('email')?.valid ?? false)
      )
      .subscribe(email => {
        this.lookupUserByEmail(email);
      });
  }

  lookupUserByEmail(email: string): void {
    this.userApiService.checkEmailExists(email).subscribe({
      next: (user) => {
        this.emailNotFound = user;
        // console.log('Email found:', user);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['families'] && changes['families'].currentValue) {
      this.patchFormValues();
    }
  }

  private patchFormValues(): void {
    this.formPatchService.patchFormValues(this.basicDetailsForm, {
      dateFields: ['dob'],
      selectFields: {
        caste: this.casteOptions,
        community: this.communityOptions,
        maritalStatus: this.maritalStatusOptions,
        docIndex: this.documentOptions
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

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  getDocumentsArray(): FormArray {
    return this.basicDetailsForm.get('personalDocuments') as FormArray;
  }

  addDocument() {
    const personalDocsArray = this.basicDetailsForm.get(
      'personalDocuments'
    ) as FormArray;
    personalDocsArray.push(this.userFormService?.createDocumentGroup());

    if (!personalDocsArray) {
      console.error('personalDocuments FormArray not found!');
      return;
    }
  }

  removeDocument(docIndex: number) {
    const personalDocsArray = this.basicDetailsForm.get(
      'personalDocuments'
    ) as FormArray;
    personalDocsArray.removeAt(docIndex);
  }

  onDocFileSelect(event: {
    file: File;
    componentIndex: number;
    docIndex: number;
  }) {
    const { file, componentIndex, docIndex } = event;

    this.fileUploadService.convertToBase64(file).then((data) => {
      const docGroup = this.getDocumentsArray().at(docIndex) as FormGroup;
      docGroup.patchValue({
        docType: data.mimeType,
        docData: data.base64,
      });
    });
  }
}
