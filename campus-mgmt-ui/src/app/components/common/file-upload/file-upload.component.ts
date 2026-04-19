import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormGroup } from '@angular/forms';
import { SharedPrimeNgModule } from '../../../shared/shared.primeng-module';

@Component({
  selector: 'app-file-upload',
  imports: [SharedPrimeNgModule],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css',
  standalone: true,
})
export class FileUploadComponent implements OnInit {
  @Input() doc!: FormGroup;
  @Input() docIndex!: number;
  @Input() componentIndex!: number;
  @Input() allDocsInArray!: FormArray;
  @Input() parentFormContext: string = '';

  @Output() remove = new EventEmitter<void>();
  @Output() fileSelected = new EventEmitter<{
    file: File;
    componentIndex: number;
    docIndex: number;
  }>();

  @Input() documentOptions: any[] = [];

  ngOnInit(): void {}
  getAcceptType(docName: string | null): string {
    return docName?.toLowerCase() === 'profile'
      ? 'image/jpeg,image/png'
      : 'application/pdf';
  }

  handleFileSelect(event: any) {
    const file = event.files?.[0];
    if (file) {
      this.fileSelected.emit({
        file,
        componentIndex: this.componentIndex,
        docIndex: this.docIndex,
      });
    }
  }

  removeDoc() {
    this.remove.emit();
  }

  getAvailableDocTypes(): any[] {
    const currentDocName = this.doc.get('docName')?.value;
    // Avoid duplicates
    const selectedNames = this.allDocsInArray.controls
      .map((c, i) => (i !== this.docIndex ? c.get('docName')?.value : null))
      .filter((name) => !!name);

    // Conditional logic based on form context
    let baseOptions = this.documentOptions;

    if (this.parentFormContext === 'Address') {
      baseOptions = baseOptions.filter((opt) =>
        [
          'Aadhar',
          'Rent Aggreement',
          'Water',
          'Electricity',
          'Broadband',
        ].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Family') {
      baseOptions = baseOptions.filter((opt) =>
        ['Aadhar', 'Others', 'Profile'].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Education') {
      baseOptions = baseOptions.filter((opt) =>
        [
          '10thMarksheet',
          '10thCertificate',
          '12thMarksheet',
          '12thCertificate',
          'Graduation',
          'Post Graduation',
          'B.Ed',
          'PHD',
          'Diploma',
          'Others'
        ].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Experience') {
      baseOptions = baseOptions.filter((opt) =>
        ['Appointment', 'Releiving', 'Others'].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Vehicle') {
      baseOptions = baseOptions.filter((opt) =>
        ['Registration', 'Driving License', 'Pollution', 'Others'].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Medical') {
      baseOptions = baseOptions.filter((opt) =>
        ['Medical', 'Discharge', 'Perscription','Others'].includes(opt.value)
      );
    } else if (this.parentFormContext === 'Personal') {
      baseOptions = baseOptions.filter((opt) =>
        ['Aadhar', 'PAN', 'Profile', 'Others'].includes(opt.value)
      );
    }

    return baseOptions.filter(
      (opt) =>
        !selectedNames.includes(opt.value) || opt.value === currentDocName
    );
  }
}
