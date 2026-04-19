package com.eduservices.student.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentHistoryDTO {
	private Long id;

	private Long studentId; // maps Student.id
	private Long enrollmentId; // maps EnrollmentEntity.id
	private Long classId; // maps ClassInfo.id
	private Long sectionId; // maps Sections.id

	private LocalDate fromDate;
	private LocalDate toDate;

}
