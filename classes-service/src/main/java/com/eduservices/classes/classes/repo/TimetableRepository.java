package com.eduservices.classes.repo;

import com.eduservices.classes.entity.TimetableEntity;
import com.eduservices.repo.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends GenericRepository<TimetableEntity, Long> {

    
}
