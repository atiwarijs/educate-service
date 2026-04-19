package com.eduservices.student.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnrollmentDTO {

	private Long id;
	private String enrollmentId; // generated at enrollment time
	private String enrolmentNo; // optional, unique number
	private Long studentId; // map Student.id
	private Long classId; // map ClassInfo.id
	private Long sectionId; // map Sections.id
	private Boolean active;
	private LocalDate enrollmentDate;
}
