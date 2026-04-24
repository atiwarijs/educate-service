package com.eduservices.teacher.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduservices.teacher.entity.Teacher;
import com.eduservices.teacher.service.TeacherService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Teacher Details", description = "This is teacher details apis")
@RestController
@RequestMapping("/teacher")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

	@PostMapping
	public ResponseEntity<Teacher> save(@RequestBody Teacher teacher) {
		logger.info("Received request to save teacher with name {}", teacher.getName());
		Teacher saved = teacherService.createTeacher(teacher);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		logger.info("Received request to soft delete teacher with id {}", id);
		teacherService.softDeleteTeacher(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Data deleted successfully!");
		return ResponseEntity.ok(response);
	}

	@PutMapping("/teachers/soft-delete")
	public ResponseEntity<Void> softDeleteTeachers(@RequestBody List<Long> ids) {
		logger.info("Received request to soft delete multiple teachers: {}", ids);
		teacherService.softDeleteTeachers(ids);
		return ResponseEntity.noContent().build();
	}
}
