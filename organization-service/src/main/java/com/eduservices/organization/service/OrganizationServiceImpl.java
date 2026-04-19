package com.eduservices.organization.service;

import com.eduservices.organization.controller.dto.OrganizationDto;
import com.eduservices.organization.controller.dto.SearchDto;
import com.eduservices.organization.entity.OrganizationEntity;
import com.eduservices.organization.repo.OrganizationRepository;
import com.eduservices.organization.exception.InvalidIdException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public OrganizationDto getOrgDetails(SearchDto dto) {

        OrganizationEntity entity = null;
        if (ObjectUtils.isEmpty(dto.getOrgCode())) {
            entity = organizationRepository.findByOrgCode(dto.getOrgCode());
        } else if (ObjectUtils.isEmpty(dto.getOrgEmailId())) {
            entity = organizationRepository.findByOrgEmailId(dto.getOrgEmailId());
        } else if (ObjectUtils.isEmpty(dto.getOrgPhoneNo())) {
            entity = organizationRepository.findByOrgPhoneNo(dto.getOrgPhoneNo());
        }
        if(entity == null){
            throw new InvalidIdException("Invalid org code or email or phone. Please pass correct details!");
        }
        return modelMapper.map(entity, OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> getOrgDetailsList(SearchDto dto) {
        List<OrganizationEntity> list = null;
        if (ObjectUtils.isEmpty(dto.getOrgType())) {
            list = organizationRepository.findByOrgType(dto.getOrgType());
        } else if (ObjectUtils.isEmpty(dto.getStatus())) {
            list = organizationRepository.findByStatus(dto.getStatus());
        } else if (ObjectUtils.isEmpty(dto.getParentOrgId())) {
            list= organizationRepository.findByParentOrgId(dto.getParentOrgId());
        }

        if(ObjectUtils.isEmpty(list)){
            throw new InvalidIdException("Invalid org status or type or parent id. Please pass correct details!");
        }
        return list.stream().map(entity -> modelMapper.map(entity, OrganizationDto.class)).toList();
    }

    @Override
    public OrganizationDto findByOrgCode(String orgCode) {
        OrganizationEntity entity = organizationRepository.findByOrgCode(orgCode);
        return entity != null ? modelMapper.map(entity, OrganizationDto.class): null;
    }

    @Override
    public OrganizationDto findByOrgEmailId(String emailId) {
        OrganizationEntity entity =  organizationRepository.findByOrgEmailId(emailId);
        return entity != null ? modelMapper.map(entity, OrganizationDto.class) : null;
    }

    @Override
    public OrganizationDto findByOrgPhoneNo(String phoneNo) {
        OrganizationEntity entity =  organizationRepository.findByOrgPhoneNo(phoneNo);
        return entity != null ? modelMapper.map(entity, OrganizationDto.class) : null;
    }
}
