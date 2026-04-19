import {
  ChangeDetectorRef,
  Component,
  Inject,
  Input,
  OnInit,
  Optional,
  SimpleChanges,
} from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { DocumentDto } from '../profile-layout/profile-form';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { ConfirmationService } from 'primeng/api';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-education',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  standalone: true,
  templateUrl: './education.component.html',
  styleUrl: './education.component.css',
})
export class EducationComponent implements OnInit {
  @Input() educationArray!: FormArray;

  statusOptions = ['Active', 'Inactive'];
  documentOptions = [
    { label: '10th Marksheet', value: '10thMarksheet' },
    { label: '10th Certificate', value: '10thCertificate' },
    { label: '12th Marksheet', value: '12thMarksheet' },
    { label: '12th Certificate', value: '12thCertificate' },
    { label: 'Diploma', value: 'Diploma' },
    { label: 'Graduation', value: 'Graduation' },
    { label: 'Post Graduation', value: 'Post Graduation' },
    { label: 'B.Ed', value: 'B.Ed' },
    { label: 'PHD', value: 'PHD' },
    { label: 'Others', value: 'Others' },
  ];
  selectedIndex: number = 0;

  constructor(
    @Optional() @Inject('educationForm') injectedForm: FormGroup,
    private userFormService: UserFormsService,
    private confirmationService: ConfirmationService,
    private fileUploadService: FileUploadService,
    private cdr: ChangeDetectorRef,
    private formPatchingService: FormPatchService
  ) {
    if (injectedForm) {
      this.educationArray.push(injectedForm);
    }
  }

  ngOnInit(): void {
    this.selectedIndex = 0;
    if (!this.educationArray || this.educationArray.length === 0) {
      this.addEducation();
    }
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['educationArray'] && changes['educationArray'].currentValue) {
      this.patchFormValues();
    }
  }
  
  private patchFormValues(): void {
    if (!this.educationArray) return;
    
    this.formPatchingService.patchFormValues(this.educationArray, {
      dateFields: ['startDate', 'endDate'],
      selectFields: {
        'docIndex': this.documentOptions,
        'status': this.statusOptions
      }
    });
    
    this.cdr.detectChanges();
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  addEducation() {
    this.educationArray.push(
      this.userFormService.createEducationGroup({} as any)
    );
    this.selectedIndex = this.educationArray.length - 1;
  }

  confirmDelete(index: number): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        this.removeEducation(index);
      },
    });
  }

  removeEducation(index: number) {
    if (this.educationArray.length > 1) {
      this.educationArray.removeAt(index);
      this.selectedIndex = this.educationArray.length - 1;
    }
  }

  getDocumentsArray(index: number): FormArray {
    const address = this.educationArray.at(index) as FormGroup;
    return address.get('educationDocuments') as FormArray;
  }

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

  removeDocument(index: number, docIndex: number) {
    const docsArray = this.educationArray
      .at(index)
      .get('educationDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }

  addDocument(index: number) {
    const addressGroup = this.educationArray.at(index) as FormGroup;
    const docsArray = addressGroup.get('educationDocuments') as FormArray;
    const newDocControl = this.userFormService.createDocumentGroup();

    newDocControl.get('docType')?.valueChanges.subscribe(() => {
      // Trigger change detection to refresh all dropdown options
      this.cdr.detectChanges();
    });
    docsArray.push(newDocControl);
  }
}
