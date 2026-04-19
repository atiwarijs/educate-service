package com.eduservices.student.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eduservices.student.dto.SectionDTO;
import com.eduservices.student.dto.StudentDto;
import com.eduservices.student.entity.ClassInfo;
import com.eduservices.student.entity.Sections;
import com.eduservices.student.entity.Student;

public class StudentMapper {

	public static StudentDto toDto(Student student) {
		if (student == null)
			return null;

		StudentDto dto = new StudentDto();
		dto.setId(student.getId());
		dto.setRegistrationNo(student.getRegistrationNo());
//		dto.setEnrolmentNo(student.getEnrolmentNo());
		dto.setName(student.getName());
		dto.setStatus(student.getStatus());
		dto.setDob(student.getDob());
		dto.setFatherName(student.getFatherName());
		dto.setMotherName(student.getMotherName());
		dto.setGender(student.getGender());
		dto.setRollNumber(student.getRollNumber());
		dto.setEmail(student.getEmail());

//		if (student.getCurrentClass() != null) {
//			dto.setClassId(student.getCurrentClass().getId());
//			dto.setClassName(student.getCurrentClass().getClassName());
//
//			if (student.getCurrentSection() != null) {
//				dto.setSectionId(student.getCurrentSection().getId());
//				dto.setSectionName(student.getCurrentSection().getSectionName() + " (Strength: "
//						+ student.getCurrentSection().getStrength() + ")");
//			}
//
//			// ✅ Proper mapping of available sections for dropdown
//			dto.setSections(student.getCurrentClass().getSections().stream().map(section -> {
//				SectionDTO sdto = new SectionDTO();
//				sdto.setId(section.getId());
//				sdto.setSectionName(section.getSectionName());
//				sdto.setStrength(section.getStrength());
//				return sdto;
//			}).collect(Collectors.toList()));
//		}

		return dto;
	}

	public static Student toEntity(StudentDto dto) {
		if (dto == null)
			return null;

		Student student = new Student();
		student.setId(dto.getId());
		student.setRegistrationNo(dto.getRegistrationNo());
//		student.setEnrolmentNo(dto.getEnrolmentNo());
		student.setName(dto.getName());
		student.setStatus(dto.getStatus());
		student.setDob(dto.getDob());
		student.setFatherName(dto.getFatherName());
		student.setMotherName(dto.getMotherName());
		student.setGender(dto.getGender());
		student.setRollNumber(dto.getRollNumber());
		student.setEmail(dto.getEmail());

//		if (dto.getClassId() != null) {
//			ClassInfo classInfo = new ClassInfo();
//			classInfo.setId(dto.getClassId());
//			student.setCurrentClass(classInfo);
//		}
//
//		if (dto.getSectionId() != null) {
//			Sections section = new Sections();
//			section.setId(dto.getSectionId());
//			student.setCurrentSection(section);
//		}

		return student;
	}
}
