package com.eduservices.student.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
	private Long id;
	private String registrationNo;
	private String rollNumber;
	private String name;
	private Date dob;
	private String gender;
	private String fatherName;
	private String motherName;
	private String email;
	private String status;

	// Instead of exposing full EnrollmentEntity, use their DTOs
	private List<EnrollmentDTO> enrollments;
}
