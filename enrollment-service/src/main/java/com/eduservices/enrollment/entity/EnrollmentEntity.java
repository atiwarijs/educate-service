package com.eduservices.enrollment.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student")
public class EnrollmentEntity extends BaseEntity {

	@Column(name = "student_id", nullable = false)
	private String studentId;

	@Column(name = "class_id", nullable = false)
	private Long classId;

	@Column(name = "enrollment_date")
	private LocalDate enrollmentDate = LocalDate.now();

}
