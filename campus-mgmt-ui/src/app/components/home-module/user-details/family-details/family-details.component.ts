import {
  ChangeDetectorRef,
  Component,
  Inject,
  Input,
  OnChanges,
  OnInit,
  Optional,
  SimpleChanges,
} from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { ConfirmationService } from 'primeng/api';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-family-details',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  standalone: true,
  templateUrl: './family-details.component.html',
  styleUrl: './family-details.component.css',
})
export class FamilyDetailsComponent implements OnInit, OnChanges {
  @Input() families!: FormArray;

  selectedFamilyIndex: number = 0;

  profileImageUrl: string = '';
  statusOptions = ['Active', 'Inactive'];

  relationOptions = [
    'Father',
    'Mother',
    'Brother',
    'Sister',
    'Spouse',
    'Child',
    'Wife',
  ];

  documentOptions = [
    { label: 'Aadhar', value: 'Aadhar' },
    { label: 'Profile Image', value: 'Profile' },
    { label: 'Others', value: 'Others' },
  ];

  constructor(
    @Optional() @Inject('familyForm') injectedForm: FormGroup,
    private userFormService: UserFormsService,
    private cdr: ChangeDetectorRef,
    private fileUploadService: FileUploadService,
    private confirmationService: ConfirmationService,
    private formPatchService: FormPatchService
  ) {
    if (injectedForm) {
      this.families.push(injectedForm);
      console.log('families array..', this.families);
    }
  }

  ngOnInit(): void {
    if (!this.families || this.families.length === 0) {
      this.addFamily();
    }
    this.selectedFamilyIndex = 0;
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['families'] && changes['families'].currentValue) {
      this.patchFormValues();
    }
  }

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

  // private ensureSelectFieldValue(
  //   formGroup: FormGroup,
  //   controlName: string,
  //   options: any[]
  // ): void {
  //   const control = formGroup.get(controlName);
  //   if (control && control.value) {
  //     const value = control.value;

  //     // For object options (with label/value structure)
  //     if (typeof options[0] === 'object') {
  //       const matchingOption = options.find(
  //         (opt) =>
  //           opt.value === value ||
  //           opt.label.toLowerCase() === value.toLowerCase()
  //       );

  //       if (matchingOption) {
  //         control.setValue(matchingOption.value);
  //       }
  //     }
  //     // For simple string array options
  //     else if (Array.isArray(options)) {
  //       const matchingOption = options.find(
  //         (opt) => opt === value || opt.toLowerCase() === value.toLowerCase()
  //       );

  //       if (matchingOption) {
  //         control.setValue(matchingOption);
  //       }
  //     }
  //   }
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

  // onDocFileSelect(event: any, familyIndex: number, docIndex: number) {
  //   const file: File = event.files?.[0];
  //   if (!file) return;

  //   this.fileUploadService.convertToBase64(file).then((data) => {
  //     const docGroup = this.getDocumentsArray(familyIndex).at(
  //       docIndex
  //     ) as FormGroup;

  //     console.log('🧾 File data before patch:', data); // ⬅️ Debug this

  //     docGroup.patchValue({
  //       docType: data.mimeType, // ✅ This should be 'image/jpeg', etc.
  //       docData: data.base64,
  //     });

  //     console.log('✅ After patch:', docGroup.value);
  //   });
  // }

  getAcceptType(docName: string | null): string {
    if (docName?.toLowerCase() === 'profile') {
      return 'image/jpeg,image/png';
    }
    return 'application/pdf';
  }

  // onProfileImageSelect(event: any, index: number): void {
  //   console.log('Upload triggered', event);
  //   const file: File = event.files?.[0];
  //   if (file) {
  //     const reader = new FileReader();
  //     reader.onload = () => {
  //       const dataURL = reader.result as string;
  //       const base64Data = dataURL.split(',')[1];

  //       const documentDto: DocumentDto = {
  //         docName: 'profileImage',
  //         docType: file.type,
  //         docData: base64Data,
  //       };

  //       // Set profile image preview
  //       this.profileImageUrl = dataURL;

  //       // Get the specific family's form group
  //       const familyGroup = this.families.at(index) as FormGroup;

  //       // Access or create memberDocuments FormArray
  //       let docsArray = familyGroup.get('memberDocuments') as FormArray;
  //       if (!docsArray) {
  //         docsArray = this.userFormService.createDocumentArray();
  //         familyGroup.addControl('memberDocuments', docsArray);
  //       }

  //       // Add the document group
  //       docsArray.push(this.userFormService.createDocumentGroup(documentDto));

  //       console.log('Uploaded file for family index:', index, documentDto);
  //     };

  //     reader.readAsDataURL(file);
  //   }
  // }

  // onAadharCardUpload(event: any, index: number): void {
  //   const file: File = event.files?.[0];
  //   if (file) {
  //     const reader = new FileReader();

  //     reader.onload = () => {
  //       const dataURL = reader.result as string;
  //       const base64Data = dataURL.split(',')[1];

  //       const documentDto: DocumentDto = {
  //         docName: file.name,
  //         docType: file.type,
  //         docData: base64Data,
  //       };

  //       const familyGroup = this.families.at(index) as FormGroup;

  //       // Access or create memberDocuments FormArray
  //       let docsArray = familyGroup.get('memberDocuments') as FormArray;
  //       if (!docsArray) {
  //         docsArray = this.userFormService.createDocumentArray();
  //         familyGroup.addControl('memberDocuments', docsArray);
  //       }

  //       // Add the document group
  //       docsArray.push(this.userFormService.createDocumentGroup(documentDto));

  //       console.log('Uploaded file for family index:', index, documentDto);
  //     };
  //     reader.readAsDataURL(file);
  //   }
  // }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  getDocumentsArray(familyIndex: number): FormArray {
    const family = this.families.at(familyIndex) as FormGroup;
    return family.get('memberDocuments') as FormArray;
  }

  addFamily() {
    this.families.push(this.userFormService.createFamilyGroup({} as any));
    this.selectedFamilyIndex = this.families.length - 1;
  }

  // Methods to remove items from form arrays
  removeFamily(index: number) {
    if (this.families.length > 1) {
      this.families.removeAt(index);
      this.selectedFamilyIndex = this.families.length - 1;
    }
  }

  confirmDelete(index: number): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete??',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        this.removeFamily(index);
      },
    });
  }

  addDocument(familyIndex: number) {
    const familyGroup = this.families.at(familyIndex) as FormGroup;
    const docsArray = familyGroup.get('memberDocuments') as FormArray;

    if (!docsArray) {
      console.error('memberDocuments FormArray not found!');
      return;
    }

    docsArray.push(this.userFormService.createDocumentGroup());
  }

  removeDocument(familyIndex: number, docIndex: number) {
    const docsArray = this.families
      .at(familyIndex)
      .get('memberDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }

  // Method to filter available document types for a specific dropdown
  // getAvailableDocTypes(familyIndex: number, currentDocIndex: number): any[] {
  //   const documentsArray = this.getDocumentsArray(familyIndex);
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
}
