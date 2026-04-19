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
import { DocumentDto } from '../profile-layout/profile-form';
import { ConfirmationService } from 'primeng/api';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-experience',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  standalone: true,
  templateUrl: './experience.component.html',
  styleUrl: './experience.component.css',
})
export class ExperienceComponent implements OnInit, OnChanges{
  @Input() experienceArray!: FormArray;

  selectedIndex: number = 0;

  statusOptions = ['Active', 'Inactive'];

  documentOptions = [
    { label: 'Appointment Letter', value: 'Appointment' },
    { label: 'Releiving Letter', value: 'Releiving' },
    { label: 'Others', value: 'Others' },
  ];

  constructor(
    @Optional() @Inject('experienceForm') injectedForm: FormGroup,
    private userFormService: UserFormsService,
    private confirmationService: ConfirmationService,
    private fileUploadService: FileUploadService,
    private cdr: ChangeDetectorRef,
    private formPatchingService: FormPatchService
  ) {
    if (injectedForm) {
      this.experienceArray.push(injectedForm);
    }
  }

  ngOnInit(): void {
    this.selectedIndex = 0;
    if (!this.experienceArray || this.experienceArray.length === 0) {
      this.addTabs();
    }
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['experienceArray'] && changes['experienceArray'].currentValue) {
      this.patchFormValues();
    }
  }
  
  private patchFormValues(): void {
    if (!this.experienceArray) return;
    
    this.formPatchingService.patchFormValues(this.experienceArray, {
      dateFields: ['joiningDate', 'leavingDate'],
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

  addTabs() {
    this.experienceArray.push(
      this.userFormService.createExperienceGroup({} as any)
    );
    this.selectedIndex = this.experienceArray.length - 1;
  }

  confirmDelete(index: number): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Yes',
      rejectLabel: 'No',
      accept: () => {
        this.removeFormGroup(index);
      },
    });
  }

  removeFormGroup(index: number) {
    if (this.experienceArray.length > 1) {
      this.experienceArray.removeAt(index);
      this.selectedIndex = this.experienceArray.length - 1;
    }
  }

  getDocumentsArray(index: number): FormArray {
    const address = this.experienceArray.at(index) as FormGroup;
    return address.get('experienceDocuments') as FormArray;
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
    const docsArray = this.experienceArray
      .at(index)
      .get('experienceDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }

  addDocument(index: number) {
    const addressGroup = this.experienceArray.at(index) as FormGroup;
    const docsArray = addressGroup.get('experienceDocuments') as FormArray;
    const newDocControl = this.userFormService.createDocumentGroup();

    newDocControl.get('docType')?.valueChanges.subscribe(() => {
      // Trigger change detection to refresh all dropdown options
      this.cdr.detectChanges();
    });
    docsArray.push(newDocControl);
  }
}
