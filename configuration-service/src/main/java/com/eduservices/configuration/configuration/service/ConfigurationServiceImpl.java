package com.eduservices.configuration.service;

import com.eduservices.configuration.controller.dto.ConfigurationDto;
import com.eduservices.configuration.controller.dto.SearchDto;
import com.eduservices.configuration.entity.ConfigurationEntity;
import com.eduservices.configuration.repo.ConfigurationRepository;
import com.eduservices.exception.InvalidIdException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    ConfigurationRepository configurationRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ConfigurationDto> searchConfigurations(SearchDto dto) {

        List<ConfigurationEntity> configurations = null;
        boolean isKeyHas = StringUtils.hasText(dto.getKey());
        boolean isOrgId = dto.getOrgId() > 0;

        if(isKeyHas && isOrgId) {
            configurations = configurationRepository.findByConfigKeyAndOrgId(dto.getKey(), dto.getOrgId());
        } else if(!isKeyHas && isOrgId) {
            configurations = configurationRepository.findByOrgId(dto.getOrgId());
        } else if(isKeyHas) {
            configurations = configurationRepository.findByConfigKey(dto.getKey());
        }

        if(ObjectUtils.isEmpty(configurations)){
            throw new InvalidIdException("Invalid key or org id, Please pass correct data");
        }
        return configurations.stream().map(entity -> modelMapper.map(entity, ConfigurationDto.class)).toList();
    }
}
