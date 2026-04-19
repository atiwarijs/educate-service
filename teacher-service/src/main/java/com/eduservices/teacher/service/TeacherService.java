package com.eduservices.teacher.service;

import java.time.Year;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.eduservices.teacher.entity.Teacher;
import com.eduservices.teacher.repo.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService {

	private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

	@Value("${school.name}")
	private String schoolName;

	private final TeacherRepository teacherRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public TeacherService(TeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

	@Transactional
	public Teacher createTeacher(Teacher teacher) {

		jdbcTemplate.update("INSERT INTO registration_sequence () VALUES ()");

		// Step 2: Fetch the last inserted ID (safe in transaction)
		Long nextId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

		// Step 3: Build registration number
		String seqFormatted = String.format("%06d", nextId);
		String regPrefix = schoolName.substring(0, 4).toUpperCase(Locale.ROOT);
		String yearSuffix = String.valueOf(Year.now().getValue()).substring(2);

		String registrationNo = regPrefix + yearSuffix + seqFormatted;

		// Step 4: Final safeguard (just in case)
		if (teacherRepository.existsByRegistrationNo(registrationNo)) {
			throw new IllegalStateException("Duplicate registration number generated: " + registrationNo);
		}

		teacher.setRegistrationNo(registrationNo);
		teacher.setStatus("REGISTERED");

		return teacherRepository.save(teacher);
	}

	public Teacher softDeleteTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> {
			log.error("Teacher with id {} not found", id);
			return new RuntimeException("Teacher not found");
		});

		teacher.setActive(false); // mark inactive
		log.debug("Teacher {} soft deleted successfully", teacher.getId());
		return teacherRepository.save(teacher);

	}

	public void softDeleteTeachers(List<Long> ids) {
		log.info("Soft deleting {} teachers", ids.size());
		List<Teacher> teachers = teacherRepository.findAllById(ids);
		teachers.forEach(t -> t.setActive(false));
		teacherRepository.saveAll(teachers);
		log.debug("Bulk soft delete completed for ids: {}", ids);
	}
}
