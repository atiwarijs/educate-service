export interface DocumentDto {
    docName: string;
    docType: string;
    docData: string;
}

export interface Personal {
    [key: string]: any; 
    id: number;
    firstName: string;
    middleName?: string;
    lastName: string;
    gander: string;
    caste: string;
    community: string;
    dob: string;
    email: string;
    mobile: string;
    alternateMobile?: string;
    alternateEmail?: string;
    maritalStatus: string;
    personalDocuments?: MemberDocument[];
}

export interface Family {
    id: number;
    memberName: string;
    memberRelation: string;
    memberGender: string;
    memberDob: string;
    memberOccupation?: string;
    memberOrg?: string;
    memberStatus: string;
    memberEmail?: string;
    memberMobile?: string;
    memberDocuments: MemberDocument[];
}

export interface Address {
    id: number;
    addressLine1: string;
    addressLine2?: string;
    unitNumber?: string;
    unitName?: string;
    locality?: string;
    city: string;
    district: string;
    state: string;
    postalCode: string;
    countryCode: string;
    countryName: string;
    latitude?: string;
    longitude?: string;
    region?: string;
    landmark: string;
    addressType: string;
    status: string;
    addressDocuments?: MemberDocument[];
    // memberDocuments: any[];
}

export interface Education {
    id: number;
    courseName: string;
    courseSubject: string;
    collegeName: string;
    collegeCity: string;
    collegeState: string;
    universityName?: string;
    boardName: string;
    startDate: string;
    endDate: string;
    obtainMarks: string;
    maxMarks: string;
    status: string;
    educationDocuments: MemberDocument[];
}

export interface Experience {
    id: number;
    institutionName: string;
    designation: string;
    skills: string;
    joiningDate: string;
    leavingDate: string;
    leavingReason: string;
    remark?: string;
    status: string;
    experienceDocuments: MemberDocument[];
}

export interface Vehicle {
    id: number;
    registrationNumber: string;
    registrationDate: string;
    chassisNumber: string;
    engineNumber: string;
    brandName: string;
    vehicleType: string;
    fuelType: string;
    dlNumber: string;
    dlExpireDate: string;
    isInsured: string;
    insuranceId: string;
    insuranceCompanyName: string;
    insuranceType: string;
    insuranceExpireDate: string;
    isPucCertificated: string;
    pucCertificateId: string;
    pucExpireDate: string;
    remark?: string;
    vehicleDocuments: MemberDocument[];
}

export interface Medical {
    id: number;
    bloodGroup: string;
    medicalHistory: string;
    diseases?: string;
    medications?: string;
    symptoms?: string;
    vitals?: string;
    diagnoses?: string;
    treatments?: string;
    therapies?: string;
    psychosocial?: string;
    medicalDocuments: MemberDocument[];
}

// Instead of this
export interface MemberDocument {
    docName: string;
    docType: string;
    docData: string;
  }

  
  
