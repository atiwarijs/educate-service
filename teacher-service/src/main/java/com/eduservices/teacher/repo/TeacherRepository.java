package com.eduservices.teacher.repo;

import com.eduservices.common.repo.GenericRepository;

import com.eduservices.teacher.entity.Teacher;

public interface TeacherRepository extends GenericRepository<Teacher, Long> {

//	@Modifying
//	@Query(value = "INSERT INTO registration_sequence () VALUES ()", nativeQuery = true)
//	void incrementSeq();
//
//	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
//	Long getLastSeq();
//
//	@Query("SELECT t.registrationNo FROM Teacher t ORDER BY t.id DESC LIMIT 1")
//	String findLastRegistrationNo();

	boolean existsByRegistrationNo(String registrationNo);

}
