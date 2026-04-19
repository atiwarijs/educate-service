package com.eduservices.student.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduservices.student.entity.ClassInfo;

@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long> {

	Optional<ClassInfo> findByClassName(String className);

}
