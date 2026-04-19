package com.eduservices.classes.controller;

import com.eduservices.classes.dto.SectionDto;
import com.eduservices.classes.dto.SubjectDto;
import com.eduservices.classes.entity.SectionEntity;
import com.eduservices.classes.entity.SubjectEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.common.controller.GenericController;
import com.eduservices.common.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Subject", description = "Subject details apis")
@RestController
@RequestMapping("/subjects")
public class SubjectController extends GenericController<SubjectDto, SubjectEntity, Long> {

    protected SubjectController(GenericRepository<SubjectEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, SubjectDto.class, SubjectEntity.class);
    }
}

