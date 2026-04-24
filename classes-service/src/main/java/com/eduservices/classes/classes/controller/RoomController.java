package com.eduservices.classes.controller;

import com.eduservices.classes.dto.RoomDto;
import com.eduservices.classes.entity.RoomEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rooms", description = "Rooms details apis")
@RestController
@RequestMapping("/rooms")
public class RoomController extends GenericController<RoomDto, RoomEntity, Long> {

    protected RoomController(GenericRepository<RoomEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, RoomDto.class, RoomEntity.class);
    }
}

