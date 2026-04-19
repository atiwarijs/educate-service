package com.eduservices.student.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduservices.student.entity.StudentHistory;

@Repository
public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Long> {
	Optional<StudentHistory> findTopByStudentIdAndToDateIsNull(Long studentId);
}
