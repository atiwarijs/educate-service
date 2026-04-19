package com.eduservices.campusmgmt.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicalDetailsDto {

    private Long id;
    @NotEmpty(message = "{medical.bloodGroup.NotEmpty}")
    private String bloodGroup;
    @NotEmpty(message = "{medical.medicalHistory.NotEmpty}")
    private String medicalHistory;
    private String diseases;
    private String medications;
    private String symptoms;
    private String vitals;
    private String diagnoses;
    private String treatments;
    private String therapies;
    private String psychosocial;
    private Set<DocumentDto> medicalDocuments;
}
