package com.eduservices.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberDto {
    private Long id;
    @NotEmpty(message = "{family.memberName.NotEmpty}")
    private String memberName;
    @NotEmpty(message = "{family.memberRelation.NotEmpty}")
    private String memberRelation;
    @NotEmpty(message = "{family.memberGender.NotEmpty}")
    private String memberGender;
    @NotEmpty(message = "{family.memberDob.NotEmpty}")
    private String memberDob;
    private String memberOccupation;
    private String memberOrg;
    @NotEmpty(message = "{family.memberStatus.status}")
    private String memberStatus;
    private String memberEmail;
    private String memberMobile;
    private Set<DocumentDto> memberDocuments;
}
