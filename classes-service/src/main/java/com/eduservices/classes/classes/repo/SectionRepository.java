package com.eduservices.classes.repo;

import com.eduservices.classes.entity.SectionEntity;
import com.eduservices.common.repo.GenericRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends GenericRepository<SectionEntity, Long> {


}
