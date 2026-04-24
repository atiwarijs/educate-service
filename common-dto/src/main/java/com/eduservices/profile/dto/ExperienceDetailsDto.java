package com.eduservices.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExperienceDetailsDto {

    private Long id;
    @NotEmpty(message = "{experience.institutionName.NotEmpty}")
    private String institutionName;
    @NotEmpty(message = "{experience.designation.NotEmpty}")
    private String designation;
    @NotEmpty(message = "{experience.skills.NotEmpty}")
    private String skills;
    @NotEmpty(message = "{experience.joiningDate.NotEmpty}")
    private String joiningDate;
    @NotEmpty(message = "{experience.leavingDate.NotEmpty}")
    private String leavingDate;
    @NotEmpty(message = "{experience.leavingReason.NotEmpty}")
    private String leavingReason;
    private String remark;
    @NotEmpty(message = "{experience.status.NotEmpty}")
    private String status;
    private Set<DocumentDto> experienceDocuments;
}
