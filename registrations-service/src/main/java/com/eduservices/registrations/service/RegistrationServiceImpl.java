package com.eduservices.registrations.service;

import com.eduservices.registrations.dto.ConfigurationDto;
import com.eduservices.registrations.dto.RegistrationDto;
import com.eduservices.registrations.dto.SearchDto;
import com.eduservices.registrations.entity.RegistrationEntity;
import com.eduservices.registrations.repo.RegistrationRepository;
import com.eduservices.registrations.service.connector.ConfigurationConnector;
import com.eduservices.registrations.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import com.eduservices.common.exception.InvalidIdException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    ConfigurationConnector configurationConnector;

    @Autowired
    RegistrationRepository registrationsRepository;

    @Autowired
    ModelMapper modelMapper;

    final String REG_PREFIX_KEY = "NEW-REGISTRATION-ID-PREFIX";

    @Override
    public RegistrationDto newRegistration(RegistrationDto dto) {
        SearchDto searchDto = new SearchDto(dto.getRegNumber(), dto.getOrgId());
        if(StringUtils.hasText(dto.getRegNumber())){
            Optional<RegistrationEntity> optionalEntity = registrationsRepository.findByRegNumber(dto.getRegNumber());
            if(optionalEntity.isPresent()){
                dto.setRegNumber(generateRegistrationNumber(searchDto));
            }
        } else {
            dto.setRegNumber(generateRegistrationNumber(searchDto));
        }
        return dto;
    }

    @Override
    public List<RegistrationDto> search(SearchDto dto) {

        List<RegistrationDto> dtoList = new ArrayList<>();
        boolean isRegNumberHas = StringUtils.hasText(dto.getKey());
        boolean isOrgIdHas = dto.getOrgId() > 0;

        if(isRegNumberHas && isOrgIdHas){
            Optional<RegistrationEntity> entity =  registrationsRepository.findByRegNumberAndOrgId(dto.getKey(), dto.getOrgId());
            entity.ifPresent(registrationEntity -> dtoList.add(modelMapper.map(registrationEntity, RegistrationDto.class)));
        } else if(!isRegNumberHas && isOrgIdHas){
            List<RegistrationEntity> list =  registrationsRepository.findByOrgId(dto.getOrgId());
            if(!list.isEmpty()){
                dtoList.addAll(list.stream().map(entity -> modelMapper.map(entity, RegistrationDto.class)).toList());
            }
        } else if(isRegNumberHas){
            Optional<RegistrationEntity> entity =  registrationsRepository.findByRegNumber(dto.getKey());
            entity.ifPresent(registrationEntity -> dtoList.add(modelMapper.map(registrationEntity, RegistrationDto.class)));
        }
        if(ObjectUtils.isEmpty(dtoList)){
            throw new InvalidIdException("Please enter correct registration number or org id!");
        }
        return dtoList;
    }

    @Override
    public String generateRegistrationNumber(SearchDto dto) {

        String registrationNumber = Utils.generateRandomNumber(10);
        List<ConfigurationDto> configurations =  configurationConnector.getConfigurations(REG_PREFIX_KEY, dto.getOrgId());
        ConfigurationDto configDto = null;
        if(configurations != null && !configurations.isEmpty()){
            configDto = configurations.get(0);
            if(configDto != null){
                registrationNumber = configDto.getConfigValue() != null ? configDto.getConfigValue()+registrationNumber : registrationNumber;
            }
        } else {
            log.info("Configuration not found against key : {} ",REG_PREFIX_KEY);
        }
        return registrationNumber;
    }

}
