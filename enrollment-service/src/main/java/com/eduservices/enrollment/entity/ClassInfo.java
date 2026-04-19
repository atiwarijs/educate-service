package com.eduservices.enrollment.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "classes_info")
public class ClassInfo extends BaseEntity {

	@Column(name = "class_name", nullable = false)
	private String className;

	@Column(name = "section", nullable = false)
	private String section;

	@Column(name = "academic_year", nullable = false)
	private String academicYear;

	@Column(name = "teacher_id", nullable = false)
	private String teacherId;

	@ElementCollection
	@CollectionTable(name = "class_students", joinColumns = @JoinColumn(name = "class_id"))
	@Column(name = "student_id")
	private List<String> studentIds = new ArrayList<>();

}
