package com.eduservices.student.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classes")
public class ClassInfo extends Auditable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "class_name", nullable = false)
	private String className;

	@Column(name = "academic_year", nullable = false)
	private String academicYear;

	@Column(name = "status")
	private String status;
//
//	@Column(name = "teacher_id")
//	private String teacherId;

	@Column(name = "active")
	private boolean active;

	@OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sections> sections = new ArrayList<>();

}
