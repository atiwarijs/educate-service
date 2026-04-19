package com.eduservices.student.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eduservices.student.dto.ClassInfoDTO;
import com.eduservices.student.dto.EnrollmentDTO;
import com.eduservices.student.dto.SectionDTO;
import com.eduservices.student.dto.StudentDto;
import com.eduservices.student.dto.StudentHistoryDTO;
import com.eduservices.student.entity.ClassInfo;
import com.eduservices.student.entity.EnrollmentEntity;
import com.eduservices.student.entity.Sections;
import com.eduservices.student.entity.Student;
import com.eduservices.student.entity.StudentHistory;

public class ClassMapper {

	// ================= STUDENT =================
	public static StudentDto toStudentDTO(Student student) {
		if (student == null)
			return null;

		StudentDto dto = new StudentDto();
		dto.setId(student.getId());
		dto.setRegistrationNo(student.getRegistrationNo());
		dto.setRollNumber(student.getRollNumber());
		dto.setName(student.getName());
		dto.setDob(student.getDob());
		dto.setGender(student.getGender());
		dto.setFatherName(student.getFatherName());
		dto.setMotherName(student.getMotherName());
		dto.setEmail(student.getEmail());
		dto.setStatus(student.getStatus());

		if (student.getEnrollments() != null) {
			dto.setEnrollments(
					student.getEnrollments().stream().map(ClassMapper::toEnrollmentDTO).collect(Collectors.toList()));
		}

		return dto;
	}

	public static Student toStudentEntity(StudentDto dto) {
		if (dto == null)
			return null;

		Student student = new Student();
		student.setId(dto.getId());
		student.setRegistrationNo(dto.getRegistrationNo());
		student.setRollNumber(dto.getRollNumber());
		student.setName(dto.getName());
		student.setDob(dto.getDob());
		student.setGender(dto.getGender());
		student.setFatherName(dto.getFatherName());
		student.setMotherName(dto.getMotherName());
		student.setEmail(dto.getEmail());
		student.setStatus(dto.getStatus());

		if (dto.getEnrollments() != null) {
			student.setEnrollments(
					dto.getEnrollments().stream().map(ClassMapper::toEnrollmentEntity).collect(Collectors.toList()));
		}

		return student;
	}

	// ================= CLASS INFO =================
	public static ClassInfoDTO toClassInfoDTO(ClassInfo classInfo) {
		if (classInfo == null)
			return null;

		ClassInfoDTO dto = new ClassInfoDTO();
		dto.setId(classInfo.getId());
		dto.setClassName(classInfo.getClassName());
		dto.setAcademicYear(classInfo.getAcademicYear());
		dto.setStatus(classInfo.getStatus());
		dto.setActive(classInfo.isActive());

		if (classInfo.getSections() != null) {
			dto.setSections(
					classInfo.getSections().stream().map(ClassMapper::toSectionDTO).collect(Collectors.toList()));
		}

		return dto;
	}

	public static ClassInfo toClassInfoEntity(ClassInfoDTO dto) {
		if (dto == null)
			return null;

		ClassInfo classInfo = new ClassInfo();
		classInfo.setId(dto.getId());
		classInfo.setClassName(dto.getClassName());
		classInfo.setAcademicYear(dto.getAcademicYear());
		classInfo.setStatus(dto.getStatus());
		classInfo.setActive(dto.isActive());

		if (dto.getSections() != null) {
			classInfo.setSections(
					dto.getSections().stream().map(ClassMapper::toSectionEntity).collect(Collectors.toList()));
			classInfo.getSections().forEach(sec -> sec.setClassEntity(classInfo));
		}

		return classInfo;
	}

	// ================= SECTIONS =================
	public static SectionDTO toSectionDTO(Sections section) {
		if (section == null)
			return null;

		SectionDTO dto = new SectionDTO();
		dto.setId(section.getId());
		dto.setSectionName(section.getSectionName());
		dto.setStrength(section.getStrength());
		dto.setCurrentEnrolled(section.getCurrentEnrolled());
		dto.setClassId(section.getClassEntity() != null ? section.getClassEntity().getId() : null);

		return dto;
	}

	public static Sections toSectionEntity(SectionDTO dto) {
		if (dto == null)
			return null;

		Sections section = new Sections();
		section.setId(dto.getId());
		section.setSectionName(dto.getSectionName());
		section.setStrength(dto.getStrength());
		section.setCurrentEnrolled(dto.getCurrentEnrolled());
		// classEntity will be set in service or parent mapper
		return section;
	}

	// ================= ENROLLMENT =================
	public static EnrollmentDTO toEnrollmentDTO(EnrollmentEntity entity) {
		if (entity == null)
			return null;

		EnrollmentDTO dto = new EnrollmentDTO();
		dto.setId(entity.getId());
		dto.setEnrollmentId(entity.getEnrollmentId());
		dto.setEnrolmentNo(entity.getEnrolmentNo());
		dto.setActive(entity.getActive());
		dto.setEnrollmentDate(entity.getEnrollmentDate());

		dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getId() : null);
		dto.setClassId(entity.getClassInfo() != null ? entity.getClassInfo().getId() : null);
		dto.setSectionId(entity.getSection() != null ? entity.getSection().getId() : null);

		return dto;
	}

	public static EnrollmentEntity toEnrollmentEntity(EnrollmentDTO dto) {
		if (dto == null)
			return null;

		EnrollmentEntity entity = new EnrollmentEntity();
		entity.setId(dto.getId());
		entity.setEnrollmentId(dto.getEnrollmentId());
		entity.setEnrolmentNo(dto.getEnrolmentNo());
		entity.setActive(dto.getActive());
		entity.setEnrollmentDate(dto.getEnrollmentDate());

		// student, classInfo, section will be set in service
		return entity;
	}

	// ================= STUDENT HISTORY =================
	public static StudentHistoryDTO toStudentHistoryDTO(StudentHistory history) {
		if (history == null)
			return null;

		StudentHistoryDTO dto = new StudentHistoryDTO();
		dto.setId(history.getId());
		dto.setFromDate(history.getFromDate());
		dto.setToDate(history.getToDate());

		dto.setStudentId(history.getStudent() != null ? history.getStudent().getId() : null);
		dto.setEnrollmentId(history.getEnrollment() != null ? history.getEnrollment().getId() : null);
		dto.setClassId(history.getClassInfo() != null ? history.getClassInfo().getId() : null);
		dto.setSectionId(history.getSection() != null ? history.getSection().getId() : null);

		return dto;
	}

	public static StudentHistory toStudentHistoryEntity(StudentHistoryDTO dto) {
		if (dto == null)
			return null;

		StudentHistory history = new StudentHistory();
		history.setId(dto.getId());
		history.setFromDate(dto.getFromDate());
		history.setToDate(dto.getToDate());
		// student, enrollment, classInfo, section set later in service
		return history;
	}

	// ================= LIST HELPERS =================
	public static List<StudentDto> toStudentDTOList(List<Student> students) {
		return students.stream().map(ClassMapper::toStudentDTO).collect(Collectors.toList());
	}

	public static List<ClassInfoDTO> toClassInfoDTOList(List<ClassInfo> classInfos) {
		return classInfos.stream().map(ClassMapper::toClassInfoDTO).collect(Collectors.toList());
	}

	public static List<SectionDTO> toSectionDTOList(List<Sections> sections) {
		return sections.stream().map(ClassMapper::toSectionDTO).collect(Collectors.toList());
	}

	public static List<EnrollmentDTO> toEnrollmentDTOList(List<EnrollmentEntity> entities) {
		return entities.stream().map(ClassMapper::toEnrollmentDTO).collect(Collectors.toList());
	}

	public static List<StudentHistoryDTO> toStudentHistoryDTOList(List<StudentHistory> histories) {
		return histories.stream().map(ClassMapper::toStudentHistoryDTO).collect(Collectors.toList());
	}
}
