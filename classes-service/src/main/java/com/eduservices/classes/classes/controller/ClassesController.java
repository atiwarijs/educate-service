package com.eduservices.classes.controller;

import com.eduservices.classes.dto.ClassDto;
import com.eduservices.classes.entity.ClassEntity;
import com.eduservices.classes.services.ClassService;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Classes", description = "Classes details apis")
@RestController
@RequestMapping("/classes")
public class ClassesController extends GenericController<ClassDto, ClassEntity, Long> {

    protected ClassesController(GenericRepository<ClassEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, ClassDto.class, ClassEntity.class);
    }

    @Autowired
    private ClassService classService;

    @GetMapping("/find/{name}")
    public List<ClassDto> searchClass(@PathVariable("name") String name){
        return classService.findByName(name);
    }

    

}

