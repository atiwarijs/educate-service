import { Injectable } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  Address,
  DocumentDto,
  Education,
  Experience,
  Family,
  Medical,
  MemberDocument,
  Personal,
  Vehicle,
} from '../../../components/home-module/user-details/profile-layout/profile-form';

@Injectable({
  providedIn: 'root',
})
export class UserFormsService {
  constructor(private fb: FormBuilder) {}

  createProfileForm(data: any): FormGroup {
    return this.fb.group({
      role: [data?.role || '', Validators.required],
      basicDetails: this.createPersonalGroup(data),
      families: this.fb.array(
        data?.families?.map((fam: Family) => this.createFamilyGroup(fam)) || []
      ),
      addresses: this.fb.array(
        this.initializeAddresses(data?.addresses)
        // data?.addresses?.map((fam: Address) => this.createAddressGroup(fam)) ||
          // []
      ),
      educations: this.fb.array(
        data?.educations?.map((edu: Education) =>
          this.createEducationGroup(edu)
        ) || []
      ),
      experiences: this.fb.array(
        data?.experiences?.map((exp: Experience) =>
          this.createExperienceGroup(exp)
        ) || []
      ),
      vehicles: this.fb.array(
        data?.vehicles?.map((veh: Vehicle) => this.createVehicleGroup(veh)) ||
          []
      ),
      medicals: this.fb.array(
        data?.medicals?.map((med: Medical) => this.createMedicalGroup(med)) ||
          []
      ),
    });
  }

  private initializeAddresses(addresses: Address[] = []): FormGroup[] {
    // Create a map of existing addresses by type
    const addressTypeMap = new Map<string, Address>();

    // Map existing addresses by their type
    addresses.forEach(addr => {
      if (addr.addressType) {
        addressTypeMap.set(addr.addressType, addr);
      }
    });

    // Always ensure we have both Permanent and Correspondence addresses
    const permanentAddress = addressTypeMap.get('Permanent') || { addressType: 'Permanent' } as Address;
    const correspondenceAddress = addressTypeMap.get('Correspondence') || { addressType: 'Correspondence' } as Address;

    // Create form groups for both addresses
    const addressForms = [
      this.createAddressGroup(permanentAddress),
      this.createAddressGroup(correspondenceAddress)
    ];

    return addressForms;
  }

  createPersonalGroup(data: Personal): FormGroup {
    return this.fb.group({
       id: [data?.id || ''],
      firstName: [data?.firstName || '', Validators.required],
      middleName: [data?.middleName || ''],
      lastName: [data?.lastName || '', Validators.required],
      gender: [data?.gander || '', Validators.required],
      caste: [data?.caste || '', Validators.required],
      community: [data?.community || '', Validators.required],
      dob: [data?.dob || '', Validators.required],
      email: [data?.email || '', [Validators.required, Validators.email]],
      mobile: [
        data?.mobile || '',
        [Validators.required, Validators.pattern('^[0-9]{10,}$')],
      ],
      alternateMobile: [data?.alternateMobile || ''],
      alternateEmail: [data?.alternateEmail || '', Validators.email],
      maritalStatus: [data?.maritalStatus || '', Validators.required],
      personalDocuments: this.fb.array(
        (data?.personalDocuments || []).map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        )
      ),
    });
  }

  createFamilyGroup(fam: Family): FormGroup {
    return this.fb.group({
      id: [fam?.id || ''],
      memberName: [fam?.memberName || '', Validators.required],
      memberRelation: [fam?.memberRelation || '', Validators.required],
      memberGender: [fam?.memberGender || '', Validators.required],
      memberDob: [fam?.memberDob || '', Validators.required],
      memberOccupation: [fam?.memberOccupation || ''],
      memberOrg: [fam?.memberOrg || ''],
      memberStatus: [fam?.memberStatus || '', Validators.required],
      memberEmail: [fam?.memberEmail || '', Validators.email],
      memberMobile: [
        fam?.memberMobile || '',
        Validators.pattern('^[0-9]{10,}$'),
      ],
      memberDocuments: this.fb.array(
        (fam?.memberDocuments || []).map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        ) || []
      ),
    });
  }

  createAddressGroup(addr: Address): FormGroup {
    return this.fb.group({
      id: [addr?.id || ''],
      addressLine1: [addr?.addressLine1 || '', Validators.required],
      addressLine2: [addr?.addressLine2 || ''],
      unitNumber: [addr?.unitNumber || ''],
      unitName: [addr?.unitName || ''],
      locality: [addr?.locality || ''],
      city: [addr?.city || '', Validators.required],
      district: [addr?.district || '', Validators.required],
      state: [addr?.state || '', Validators.required],
      postalCode: [addr?.postalCode || '', Validators.required],
      countryCode: [addr?.countryCode || '', Validators.required],
      countryName: [addr?.countryName || '', Validators.required],
      latitude: [addr?.latitude || ''],
      longitude: [addr?.longitude || ''],
      region: [addr?.region || ''],
      landmark: [addr?.landmark || '', Validators.required],
      addressType: [addr?.addressType || '', Validators.required],
      status: [addr?.status || '', Validators.required],
      // addressDocuments: [addr?.addressDocuments || null],
      addressDocuments: this.fb.array(
        addr?.addressDocuments?.map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        ) || []
      ),
    });
  }

  createEducationGroup(edu: Education): FormGroup {
    return this.fb.group({
      id: [edu?.id || ''],
      courseName: [edu?.courseName || '', Validators.required],
      courseSubject: [edu?.courseSubject || '', Validators.required],
      collegeName: [edu?.collegeName || '', Validators.required],
      collegeCity: [edu?.collegeCity || '', Validators.required],
      collegeState: [edu?.collegeState || '', Validators.required],
      universityName: [edu?.universityName || ''],
      boardName: [edu?.boardName || '', Validators.required],
      startDate: [edu?.startDate || '', Validators.required],
      endDate: [edu?.endDate || '', Validators.required],
      obtainMarks: [edu?.obtainMarks || '', Validators.required],
      maxMarks: [edu?.maxMarks || '', Validators.required],
      status: [edu?.status || '', Validators.required],
      educationDocuments: this.fb.array(
        edu?.educationDocuments?.map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        ) || []
      ),
    });
  }

  createExperienceGroup(exp: Experience): FormGroup {
    return this.fb.group({
      id: [exp?.id || ''],
      institutionName: [exp?.institutionName || '', Validators.required],
      designation: [exp?.designation || '', Validators.required],
      skills: [exp?.skills || '', Validators.required],
      joiningDate: [exp?.joiningDate || '', Validators.required],
      leavingDate: [exp?.leavingDate || '', Validators.required],
      leavingReason: [exp?.leavingReason || '', Validators.required],
      remark: [exp?.remark || ''],
      status: [exp?.status || '', Validators.required],
      experienceDocuments: this.fb.array(
        exp?.experienceDocuments?.map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        ) || []
      ),
    });
  }

  createVehicleGroup(v: Vehicle = {} as Vehicle): FormGroup {
    // Fix for the undefined map error
    return this.fb.group({
      id: [v?.id || ''],
      registrationNumber: [v?.registrationNumber || '', Validators.required],
      registrationDate: [v?.registrationDate || '', Validators.required],
      chassisNumber: [v?.chassisNumber || '', Validators.required],
      engineNumber: [v?.engineNumber || '', Validators.required],
      brandName: [v?.brandName || '', Validators.required],
      vehicleType: [v?.vehicleType || '', Validators.required],
      fuelType: [v?.fuelType || '', Validators.required],
      dlNumber: [v?.dlNumber || '', Validators.required],
      dlExpireDate: [v?.dlExpireDate || '', Validators.required],
      isInsured: [v?.isInsured || '', Validators.required],
      insuranceId: [v?.insuranceId || '', Validators.required],
      insuranceCompanyName: [
        v?.insuranceCompanyName || '',
        Validators.required,
      ],
      insuranceType: [v?.insuranceType || '', Validators.required],
      insuranceExpireDate: [v?.insuranceExpireDate || '', Validators.required],
      isPucCertificated: [v?.isPucCertificated || '', Validators.required],
      pucCertificateId: [v?.pucCertificateId || '', Validators.required],
      pucExpireDate: [v?.pucExpireDate || '', Validators.required],
      remark: [v?.remark || ''],
      vehicleDocuments: this.fb.array(
        (v?.vehicleDocuments || []).map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        )
      ),
    });
  }

  createMedicalGroup(m: Medical = {} as Medical): FormGroup {
    return this.fb.group({
      id: [m?.id || ''],
      bloodGroup: [m?.bloodGroup || '', Validators.required],
      medicalHistory: [m?.medicalHistory || '', Validators.required],
      diseases: [m?.diseases || ''],
      medications: [m?.medications || ''],
      symptoms: [m?.symptoms || ''],
      vitals: [m?.vitals || ''],
      diagnoses: [m?.diagnoses || ''],
      treatments: [m?.treatments || ''],
      therapies: [m?.therapies || ''],
      psychosocial: [m?.psychosocial || ''],
      medicalDocuments: this.fb.array(
        (m?.medicalDocuments || []).map((doc: MemberDocument) =>
          this.createDocumentGroup(doc)
        )
      ),
    });
  }

  createDocumentGroup(doc: any = {}): FormGroup {
    return this.fb.group({
      docName: [doc?.docName || ''],
      docType: [doc?.docType || ''],
      docData: [doc?.docData || ''],
    });
  }

  // Add this method to your UserFormsService
  createEmptyFormArrayFor(section: string): FormArray {
    const fb = new FormBuilder();

    // Create appropriate empty form structure based on section
    let emptyFormGroup: FormGroup;

    switch (section) {
      case 'family':
        emptyFormGroup = fb.group({
          memberName: ['', Validators.required],
          memberRelation: ['', Validators.required],
          memberGender: ['', Validators.required],
          memberDob: [null, Validators.required],
          memberOccupation: [''],
          memberOrg: [''],
          memberStatus: ['', Validators.required],
          memberEmail: ['', Validators.email],
          memberMobile: [
            '',
            [Validators.maxLength(10), Validators.pattern(/^\d{10}$/)],
          ],
        });
        break;
      case 'address':
        emptyFormGroup = fb.group({
          addressLine1: ['', Validators.required],
          addressLine2: [''],
          unitNumber: [''],
          locality: [''],
          city: ['', Validators.required],
          district: ['', Validators.required],
          state: ['', Validators.required],
          postalCode: ['', Validators.required],
          countryCode: ['', Validators.required],
          landmark: [''],
          addressProof: [null],
        });
        break;
      // Add cases for other sections as needed
      default:
        // Generic empty form if specific structure is unknown
        emptyFormGroup = fb.group({});
    }

    return fb.array([emptyFormGroup]);
  }

  createDocumentArray(initialDocs: DocumentDto[] = []): FormArray {
    const docGroups = initialDocs.map((doc) => this.createDocumentGroup(doc));
    return this.fb.array(docGroups);
  }
}
