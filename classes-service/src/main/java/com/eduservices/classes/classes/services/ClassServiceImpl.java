package com.eduservices.classes.services;

import com.eduservices.classes.dto.ClassDto;
import com.eduservices.classes.entity.ClassEntity;
import com.eduservices.classes.repo.ClassRepository;
import com.eduservices.exception.NoContentException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ClassDto> findByName(String name) {
        List<ClassEntity> classes = classRepository.findByName(name);
        if(classes.isEmpty()){
            throw new NoContentException("Classes data not found!");
        }
        return classes.stream().map(entity -> modelMapper.map(entity, ClassDto.class)).toList();
    }
}
