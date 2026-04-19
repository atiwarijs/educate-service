package com.eduservices.student.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eduservices.student.dto.StudentDto;
import com.eduservices.student.entity.ClassInfo;
import com.eduservices.student.entity.Sections;
import com.eduservices.student.entity.Student;
import com.eduservices.student.entity.StudentHistory;
import com.eduservices.student.mapper.StudentMapper;
import com.eduservices.student.repo.StudentHistoryRepository;
import com.eduservices.student.repo.StudentRepository;

@Service
public class StudentService {

	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	@Value("${school.name}")
	private String schoolName;

	private final StudentRepository studentRepository;

	private final StudentHistoryRepository historyRepository;

	public StudentService(StudentRepository studentRepository, StudentHistoryRepository historyRepository) {
		this.studentRepository = studentRepository;
		this.historyRepository = historyRepository;
	}

	public Student createStudent(Student student) {

		long nextId = studentRepository.count() + 1;
		String seqFormatted = String.format("%06d", nextId);

		String regPrefix = schoolName.substring(0, 4).toUpperCase(Locale.ROOT);
		String yearSuffix = String.valueOf(Year.now().getValue()).substring(2);
		String registrationNo = regPrefix + yearSuffix + seqFormatted; // TECH25000001

		log.info("Generating numbers for student [{}]: registrationNo={}", student.getName(), registrationNo);

		student.setRegistrationNo(registrationNo);
		student.setStatus("REGISTERED");
		log.debug("Student [{}] persisted with ID={} registrationNo={}", student.getName(), student.getId(),
				student.getRegistrationNo());

		return studentRepository.save(student);
	}

	public StudentDto updateStudent(StudentDto updatedDto) {
		Student existingStudent = studentRepository.findById(updatedDto.getId())
				.orElseThrow(() -> new RuntimeException("Student not found with id " + updatedDto.getId()));

		// Convert DTO → Entity for processing
		Student updatedStudent = StudentMapper.toEntity(updatedDto);

//		handleStatusChange(existingStudent, updatedStudent);
//		handleClassAndSectionChange(existingStudent, updatedStudent);

		Student saved = studentRepository.save(updatedStudent);
//		logStudentUpdate(saved);

		// Convert Entity → DTO for return
		return StudentMapper.toDto(saved);
	}

//	private void handleStatusChange(Student existingStudent, Student updatedStudent) {
//		if ("REGISTERED".equalsIgnoreCase(existingStudent.getStatus())
//				&& "ENROLLED".equalsIgnoreCase(updatedStudent.getStatus())) {
//			log.info("Generating enrolment number for student: {}", updatedStudent.getName());
//			String enrolmentNo = generateEnrolmentNo(updatedStudent);
//			updatedStudent.setEnrolmentNo(enrolmentNo);
//		}
//	}	

//	@Transactional
//	private void handleClassAndSectionChange(Student existingStudent, Student updatedStudent) {
//		Long existingClassId = existingStudent.getCurrentClass() != null ? existingStudent.getCurrentClass().getId()
//				: null;
//		Long existingSectionId = existingStudent.getCurrentSection() != null
//				? existingStudent.getCurrentSection().getId()
//				: null;
//
//		Long newClassId = updatedStudent.getCurrentClass() != null ? updatedStudent.getCurrentClass().getId() : null;
//		Long newSectionId = updatedStudent.getCurrentSection() != null ? updatedStudent.getCurrentSection().getId()
//				: null;
//
//		// check if class or section is changed
//		if (!Objects.equals(existingClassId, newClassId) || !Objects.equals(existingSectionId, newSectionId)) {
//
//			// close old history (set toDate if open history exists)
//			historyRepository.findTopByStudentIdAndToDateIsNull(existingStudent.getId()).ifPresent(history -> {
//				history.setToDate(LocalDate.now());
//				historyRepository.save(history);
//			});
//
//			// create new student history
//			StudentHistory newHistory = new StudentHistory();
//			newHistory.setStudent(existingStudent); // student ref
//			newHistory.setClassInfo(updatedStudent.getCurrentClass()); // ClassInfo entity
//			newHistory.setSection(updatedStudent.getCurrentSection()); // Sections entity
//			newHistory.setFromDate(LocalDate.now());
//			newHistory.setToDate(null);
//
//			historyRepository.save(newHistory);
//
//			log.info("Student {} moved from class {} section {} → class {} section {}", existingStudent.getId(),
//					existingClassId, existingSectionId, newClassId, newSectionId);
//		}
//	}
//
//	private void logStudentUpdate(Student saved) {
//		log.info("Student updated: id={}, regNo={}, enrolmentNo={}, status={}, classId={}, sectionId={}", saved.getId(),
//				saved.getRegistrationNo(), saved.getEnrolmentNo(), saved.getStatus(),
//				saved.getCurrentClass() != null ? saved.getCurrentClass().getId() : null,
//				saved.getCurrentSection() != null ? saved.getCurrentSection().getId() : null);
//	}

	private String generateEnrolmentNo(Student student) {

		String enrolmentPrefix = schoolName.substring(0, 3).toUpperCase(Locale.ROOT);

		long seq = studentRepository.count() + 1;
		String seqFormatted = String.format("%06d", seq);
		String yearSuffix = String.valueOf(Year.now().getValue()).substring(2);
		return enrolmentPrefix + yearSuffix + seqFormatted;
	}

	public Student softDeleteStudent(Long id) {
		Student teacher = studentRepository.findById(id).orElseThrow(() -> {
			log.error("Student with id {} not found", id);
			return new RuntimeException("Student not found");
		});

		teacher.setActive(false); // mark inactive
		log.debug("Student {} soft deleted successfully", teacher.getId());
		return studentRepository.save(teacher);

	}

	public void softDeleteStudents(List<Long> ids) {
		log.info("Soft deleting {} teachers", ids.size());
		List<Student> teachers = studentRepository.findAllById(ids);
		teachers.forEach(t -> t.setActive(false));
		studentRepository.saveAll(teachers);
		log.debug("Bulk soft delete completed for ids: {}", ids);
	}

}
