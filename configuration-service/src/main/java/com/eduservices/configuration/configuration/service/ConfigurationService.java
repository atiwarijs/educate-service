package com.eduservices.configuration.service;

import com.eduservices.configuration.controller.dto.ConfigurationDto;
import com.eduservices.configuration.controller.dto.SearchDto;
import com.eduservices.configuration.entity.ConfigurationEntity;

import java.util.List;

public interface ConfigurationService {

    List<ConfigurationDto> searchConfigurations(SearchDto searchPayload);
 }
