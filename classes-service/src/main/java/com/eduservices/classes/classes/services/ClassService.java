package com.eduservices.classes.services;

import com.eduservices.classes.dto.ClassDto;

import java.util.List;

public interface ClassService {

    List<ClassDto> findByName(String name);
}
