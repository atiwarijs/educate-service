package com.eduservices.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SectionDTO {
	private Long id;
	private String sectionName;
	private Long classId; // map ClassInfo.id instead of whole entity
	private Integer strength;
	private Integer currentEnrolled;
}
