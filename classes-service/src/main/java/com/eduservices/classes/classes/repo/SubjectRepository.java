package com.eduservices.classes.repo;

import com.eduservices.classes.entity.ClassEntity;
import com.eduservices.classes.entity.SubjectEntity;
import com.eduservices.common.repo.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends GenericRepository<SubjectEntity, Long> {

    
}
