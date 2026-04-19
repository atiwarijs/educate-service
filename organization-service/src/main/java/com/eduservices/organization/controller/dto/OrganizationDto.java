package com.eduservices.organization.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    private Long id;
    @NotEmpty(message = "{org.name}")
    private String orgName;
    @NotEmpty(message = "{org.code}")
    private String orgCode;
    @NotEmpty(message = "{org.title}")
    private String orgTitle;
    private String orgSubTitle;
    @NotEmpty(message = "{org.location}")
    private String orgLocation;
    @NotEmpty(message = "{org.website}")
    private String orgWebsiteLink;
    @NotEmpty(message = "{org.email}")
    private String orgEmailId;
    @NotEmpty(message = "{org.fax}")
    private String orgFaxNo;
    @NotEmpty(message = "{org.phone}")
    private String orgPhoneNo;
    private String orgType;
    private String status;
    private Long parentOrgId;
}
