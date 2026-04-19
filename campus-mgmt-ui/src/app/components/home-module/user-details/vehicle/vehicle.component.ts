import { ChangeDetectorRef, Component, Inject, Input, OnChanges, OnInit, Optional, SimpleChanges } from '@angular/core';
import { AbstractControl, FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../../shared/shared.primeng-module';
import { DocumentDto } from '../profile-layout/profile-form';
import { UserFormsService } from '../../../../services/profile-service/services/user-details-form-service';
import { FileUploadService } from '../../../../services/profile-service/services/file-upload.service';
import { ConfirmationService } from 'primeng/api';
import { FileUploadComponent } from "../../../common/file-upload/file-upload.component";
import { SharedModule } from '../../../../shared/shared-module.module';
import { FormPatchService } from '../../../../services/profile-service/form-patch-service.service';

@Component({
  selector: 'app-vehicle',
  imports: [SharedPrimeNgModule, FileUploadComponent, SharedModule],
  templateUrl: './vehicle.component.html',
  styleUrl: './vehicle.component.css',
})
export class VehicleComponent implements OnInit, OnChanges{
  @Input() vehicleArray!: FormArray;

  selectedIndex: number = 0;

  vehicleTypes = ['Car', 'Motorcycle', 'Truck', 'Van'];
  fuelTypes = ['Petrol', 'Diesel', 'Electric', 'Hybrid'];
  documentOptions = [
    { label: 'Registration Certificate', value: 'Registration' },
    { label: 'Driving License', value: 'Driving License' },
    { label: 'Pollution Certificate', value: 'Pollution' },
    { label: 'Others', value: 'Others' },
  ];

  constructor(
    @Optional() @Inject('vehicleForm') injectedForm: FormGroup,
    private userFormService: UserFormsService,
    private confirmationService: ConfirmationService,
    private fileUploadService: FileUploadService,
    private cdr: ChangeDetectorRef,
    private formPatchService: FormPatchService
  ) {
    if (injectedForm) {
      this.vehicleArray.push(injectedForm);
    }
  }

  ngOnInit(): void {
    this.selectedIndex = 0;
    if (!this.vehicleArray || this.vehicleArray.length === 0) {
      this.addTabs();
    }
    this.patchFormValues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleArray'] && changes['vehicleArray'].currentValue) {
      this.patchFormValues();
    }
  }

  private patchFormValues(): void {
    if (!this.vehicleArray) return;

    this.formPatchService.patchFormValues(this.vehicleArray, {
      dateFields: ['registrationDate','dlExpireDate', 'pucExpireDate', 'insuranceExpireDate'],
      selectFields: {
        bloodGroup: this.vehicleTypes,
        fuelType: this.fuelTypes,
        docIndex: this.documentOptions
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

    // Trigger change detection to ensure UI updates
    this.cdr.detectChanges();
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  addTabs() {
    this.vehicleArray.push(this.userFormService.createVehicleGroup({} as any));
    this.selectedIndex = this.vehicleArray.length - 1;
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
    if (this.vehicleArray.length > 1) {
      this.vehicleArray.removeAt(index);
      this.selectedIndex = this.vehicleArray.length - 1;
    }
  }

  getDocumentsArray(index: number): FormArray {
    const address = this.vehicleArray.at(index) as FormGroup;
    return address.get('vehicleDocuments') as FormArray;
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
    const docsArray = this.vehicleArray
      .at(index)
      .get('vehicleDocuments') as FormArray;
    docsArray.removeAt(docIndex);
  }

  addDocument(index: number) {
    const addressGroup = this.vehicleArray.at(index) as FormGroup;
    const docsArray = addressGroup.get('vehicleDocuments') as FormArray;
    const newDocControl = this.userFormService.createDocumentGroup();

    newDocControl.get('docType')?.valueChanges.subscribe(() => {
      // Trigger change detection to refresh all dropdown options
      this.cdr.detectChanges();
    });
    docsArray.push(newDocControl);
  }
}
