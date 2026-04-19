package com.eduservices.student.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClassInfoDTO {
	private Long id;
	private String className;
	private String academicYear;
	private String status;
	private boolean active;
	private List<SectionDTO> sections;
}
