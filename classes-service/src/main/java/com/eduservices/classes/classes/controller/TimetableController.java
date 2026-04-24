package com.eduservices.classes.controller;

import com.eduservices.classes.dto.TimetableDto;
import com.eduservices.classes.entity.TimetableEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Timetables", description = "Timetables details apis")
@RestController
@RequestMapping("/timetables")
public class TimetableController extends GenericController<TimetableDto, TimetableEntity, Long> {

    protected TimetableController(GenericRepository<TimetableEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, TimetableDto.class, TimetableEntity.class);
    }
}

