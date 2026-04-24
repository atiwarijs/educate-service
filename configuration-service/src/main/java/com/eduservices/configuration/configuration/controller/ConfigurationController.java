package com.eduservices.configuration.controller;

import com.eduservices.configuration.controller.dto.ConfigurationDto;
import com.eduservices.configuration.controller.dto.SearchDto;
import com.eduservices.configuration.entity.ConfigurationEntity;
import com.eduservices.configuration.service.ConfigurationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Configurations", description = "Configurations management api")
@RestController
@RequestMapping("/configurations")
public class ConfigurationController extends GenericController<ConfigurationDto, ConfigurationEntity, Long> {


    @Autowired
    ConfigurationService configurationService;

    protected ConfigurationController(
            GenericRepository<ConfigurationEntity, Long> genericRepository,
            ModelMapper mapper) {
        super(genericRepository, mapper, ConfigurationDto.class, ConfigurationEntity.class);
    }

    @PostMapping("/search")
    public List<ConfigurationDto> getConfigurations(@RequestBody SearchDto searchPayload) {
        return configurationService.searchConfigurations(searchPayload);
    }


}
