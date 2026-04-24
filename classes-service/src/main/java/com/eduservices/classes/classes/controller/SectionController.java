package com.eduservices.classes.controller;

import com.eduservices.classes.dto.SectionDto;
import com.eduservices.classes.entity.SectionEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sections", description = "Section details apis")
@RestController
@RequestMapping("/sections")
public class SectionController extends GenericController<SectionDto, SectionEntity, Long> {

    protected SectionController(GenericRepository<SectionEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, SectionDto.class, SectionEntity.class);
    }
}

