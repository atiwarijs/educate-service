package com.eduservices.campusmgmt.dto;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDetailsDto {

    private Long id;
    private String userId;
    @NotEmpty(message = "{personal.firstName.NotEmpty}")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "{personal.lastName.NotEmpty}")
    private String lastName;
    @NotEmpty(message = "{personal.gender.NotEmpty}")
    private String gender;
    @NotEmpty(message = "{personal.caste.NotEmpty}")
    private String caste;
    @NotEmpty(message = "{personal.community.NotEmpty}")
    private String community;
    @NotEmpty(message = "{personal.dob.NotEmpty}")
    private String dob;
    @NotEmpty(message = "{personal.email.NotEmpty}")
    private String email;
    @NotEmpty(message = "{personal.mobile.NotEmpty}")
    private String mobile;
    private String profileStatus;
    private boolean status;
    private String alternateMobile;
    private String alternateEmail;
    @NotEmpty(message = "{personal.maritalStatus.NotEmpty}")
    private String maritalStatus;
    private Set<DocumentDto> personalDocuments;
    @Valid
    private Set<FamilyMemberDto> families;
    @Valid
    private Set<AddressDetailsDto> addresses;
    @Valid
    private Set<EducationDetailsDto> educations;
    @Valid
    private Set<ExperienceDetailsDto> experiences;
    @Valid
    private Set<VehicleDetailsDto> vehicles;
    @Valid
    private Set<MedicalDetailsDto> medicals;
}
