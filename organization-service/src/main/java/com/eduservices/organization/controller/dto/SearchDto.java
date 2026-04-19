package com.eduservices.organization.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDto {

    private String orgCode;
    private String orgEmailId;
    private String orgPhoneNo;
    private String orgType;
    private String status;
    private Long parentOrgId;

}
