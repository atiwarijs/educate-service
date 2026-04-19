package com.eduservices.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduservices.enrollment.entity.ClassInfo;

@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long>{

}
