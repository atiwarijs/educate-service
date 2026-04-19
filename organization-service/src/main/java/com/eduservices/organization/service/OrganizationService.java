package com.eduservices.organization.service;
import com.eduservices.organization.controller.dto.OrganizationDto;
import com.eduservices.organization.controller.dto.SearchDto;

import java.util.List;

public interface OrganizationService {

    OrganizationDto getOrgDetails(SearchDto dto);

    List<OrganizationDto> getOrgDetailsList(SearchDto dto);

    OrganizationDto findByOrgCode(String orgCode);

    OrganizationDto findByOrgEmailId(String emailId);

    OrganizationDto findByOrgPhoneNo(String phoneNo);

}
