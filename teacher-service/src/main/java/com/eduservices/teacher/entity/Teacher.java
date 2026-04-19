package com.eduservices.teacher.entity;

import java.util.Date;

import com.eduservices.profile.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "teacher")
public class Teacher extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_seq")
	@SequenceGenerator(name = "teacher_seq", sequenceName = "teacher_seq", allocationSize = 1)
	private Long id;

	@Column(name = "employee_no", nullable = true, unique = true)
	private String employeeNo;

	@Column(name = "registration_no", nullable = false, unique = true)
	private String registrationNo;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "dob")
	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name = "gender", length = 10)
	private String gender;

	@Column(name = "qualification", length = 150)
	private String qualification;

	@Column(name = "specialization", length = 150)
	private String specialization;

	@Column(name = "email", unique = true, length = 100)
	private String email;

	@Column(name = "phone", length = 15, unique = true)
	private String phone;

	@Column(name = "joining_date")
	@Temporal(TemporalType.DATE)
	private Date joiningDate;
	
	@Column(name = "status")
	private String status;

}
