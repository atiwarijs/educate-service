package com.eduservices.student.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduservices.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
