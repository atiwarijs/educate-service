package com.eduservices.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eduservices.student.exception.NoContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduservices.student.dto.StudentDto;
import com.eduservices.student.entity.Student;
import com.eduservices.student.mapper.StudentMapper;
import com.eduservices.student.repo.StudentRepository;
import com.eduservices.student.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Student Details", description = "This is student details apis")
@RestController
@RequestMapping("/student")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentRepository studentRepository;

	@PostMapping
	public ResponseEntity<Student> save(@RequestBody Student student) {
		logger.info("Received request to save Student with name {}", student.getName());
		Student saved = studentService.createStudent(student);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("/update")
	public ResponseEntity<StudentDto> update(@RequestBody @Valid StudentDto student) {
		logger.info("Received request to update Student with name {}", student.getName());
		StudentDto saved = studentService.updateStudent(student);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		logger.info("Received request to soft delete student with id {}", id);
		studentService.softDeleteStudent(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Data deleted successfully!");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/students/soft-delete")
	public ResponseEntity<Void> softDeleteStudents(@RequestBody List<Long> ids) {
		logger.info("Received request to soft delete multiple students: {}", ids);
		studentService.softDeleteStudents(ids);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/fetch/list")
	public ResponseEntity<?> getAllStudents() {
		List<Student> students = studentRepository.findAll();
		if (ObjectUtils.isEmpty(students)) {
			throw new NoContentException("No content found!");
		}
		List<StudentDto> dtos = students.stream().map(StudentMapper::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtos);
	}

}
