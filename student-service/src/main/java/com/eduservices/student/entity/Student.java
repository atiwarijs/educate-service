package com.eduservices.student.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eduservices.profile.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "registration_no", nullable = false, unique = true)
	private String registrationNo;

	@Column(unique = true)
	private String rollNumber;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "dob")
	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name = "gender", length = 10)
	private String gender;

	@Column(name = "father_name", length = 100)
	private String fatherName;

	@Column(name = "mother_name", length = 100)
	private String motherName;

	@Column(name = "email", unique = true, length = 100)
	private String email;

	@Column(name = "status")
	private String status;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EnrollmentEntity> enrollments = new ArrayList<>();

	// Helper method to get enrollment IDs - renamed to avoid conflict
	@Transient
	public List<Long> getEnrollmentIds() {
		return enrollments == null ? null : enrollments.stream().map(EnrollmentEntity::getId).toList();
	}
}
