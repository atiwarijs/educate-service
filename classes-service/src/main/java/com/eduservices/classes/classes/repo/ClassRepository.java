package com.eduservices.classes.repo;

import com.eduservices.classes.entity.ClassEntity;
import com.eduservices.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends GenericRepository<ClassEntity, Long> {

    List<ClassEntity> findByName(String name);
    
}
