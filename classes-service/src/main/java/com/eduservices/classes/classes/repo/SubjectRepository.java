package com.eduservices.classes.repo;

import com.eduservices.classes.entity.SubjectEntity;
import com.eduservices.repo.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends GenericRepository<SubjectEntity, Long> {

    
}
