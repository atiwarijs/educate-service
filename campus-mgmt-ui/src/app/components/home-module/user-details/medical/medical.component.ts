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
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { DocumentDto } from '../profile-layout/profile-form';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { FileUploadComponent } from '../../../common/file-upload/file-upload.component';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { ConfirmationService } from 'primeng/api';
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-medical',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  standalone: true,
  templateUrl: './medical.component.html',
  styleUrl: './medical.component.css',
})
export class MedicalComponent implements OnInit, OnChanges{
  @Input() medicalsArray!: FormArray;
  bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
  selectedIndex: number = 0;

  documentOptions = [
    { label: 'Medical Bill', value: 'Medical' },
    { label: 'Discharge summary', value: 'Discharge' },
    { label: 'Doctor Perscription', value: 'Perscription' },
    { label: 'Others', value: 'Others' },
  ];

  constructor(
    @Optional() @Inject('medicalForm') injectedForm: FormGroup,
    private userFormService: UserFormsService,
    private confirmationService: ConfirmationService,
    private fileUploadService: FileUploadService,
    private cdr: ChangeDetectorRef,
    private formPatchService: FormPatchService
  ) {
    if (injectedForm) {
      this.medicalsArray.push(injectedForm);
    }
  }

  ngOnInit(): void {
    if (!this.medicalsArray || this.medicalsArray.length === 0) {
      this.addTabs();
    }
    this.selectedIndex = 0;
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['medicalsArray'] && changes['medicalsArray'].currentValue) {
      this.patchFormValues();
    }
  }

  private patchFormValues(): void {
    if (!this.medicalsArray) return;

    this.formPatchService.patchFormValues(this.medicalsArray, {
      dateFields: ['memberDob'],
      selectFields: {
        bloodGroup: this.bloodGroups
      }
    });

    // Trigger change detection to ensure UI updates
    this.cdr.detectChanges();
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  addTabs() {
    this.medicalsArray.push(this.userFormService.createMedicalGroup({} as any));
    this.selectedIndex = this.medicalsArray.length - 1;
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
    if (this.medicalsArray.length > 1) {
      this.medicalsArray.removeAt(index);
      this.selectedIndex = this.medicalsArray.length - 1;
    }
  }

  getDocumentsArray(index: number): FormArray {
    const address = this.medicalsArray.at(index) as FormGroup;
    return address.get('medicalDocuments') as FormArray;
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
    const docsArray = this.medicalsArray
      .at(index)
      .get('medicalDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }

  addDocument(index: number) {
    const addressGroup = this.medicalsArray.at(index) as FormGroup;
    const docsArray = addressGroup.get('medicalDocuments') as FormArray;
    const newDocControl = this.userFormService.createDocumentGroup();

    newDocControl.get('docType')?.valueChanges.subscribe(() => {
      // Trigger change detection to refresh all dropdown options
      this.cdr.detectChanges();
    });
    docsArray.push(newDocControl);
  }
}
