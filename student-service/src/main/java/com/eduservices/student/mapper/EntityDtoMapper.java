package com.eduservices.student.mapper;

//@Mapper(componentModel = "spring")
public interface EntityDtoMapper {
//
//	// ---------------- Student ----------------
//	@Mapping(target = "enrollments", source = "enrollments") // Maps List<EnrollmentEntity> to List<EnrollmentDTO>
//	StudentDto toDto(Student entity);
//
//	@Mapping(target = "enrollments", ignore = true) // Handle in service layer
//	Student toEntity(StudentDto dto);
//
//	List<StudentDto> toStudentDtoList(List<Student> entities);
//
//	// ---------------- ClassInfo ----------------
//	@Mapping(target = "sections", source = "sections")
//	ClassInfoDTO toDto(ClassInfo entity);
//
//	@Mapping(target = "sections", ignore = true) // Handle in service layer
//	ClassInfo toEntity(ClassInfoDTO dto);
//
//	List<ClassInfoDTO> toClassInfoDtoList(List<ClassInfo> entities);
//
//	// ---------------- Sections ----------------
//	@Mapping(target = "classId", source = "classEntity.id")
//	SectionDTO toDto(Sections entity);
//
//	@Mapping(target = "classEntity", ignore = true) // Set manually in service
//	@Mapping(target = "id", source = "id")
//	Sections toEntity(SectionDTO dto);
//
//	List<SectionDTO> toSectionDtoList(List<Sections> entities);
//
//	// ---------------- Enrollment ----------------
//	@Mapping(target = "studentId", source = "student.id")
//	@Mapping(target = "classId", source = "classInfo.id")
//	@Mapping(target = "sectionId", source = "section.id")
//	EnrollmentDTO toDto(EnrollmentEntity entity);
//
//	@Mapping(target = "student", ignore = true) // Handle in service
//	@Mapping(target = "classInfo", ignore = true) // Handle in service
//	@Mapping(target = "section", ignore = true) // Handle in service
//	EnrollmentEntity toEntity(EnrollmentDTO dto);
//
//	List<EnrollmentDTO> toEnrollmentDtoList(List<EnrollmentEntity> entities);
//
//	// ---------------- StudentHistory ----------------
//	@Mapping(target = "studentId", source = "student.id")
//	@Mapping(target = "enrollmentId", source = "enrollment.id")
//	@Mapping(target = "classId", source = "classInfo.id")
//	@Mapping(target = "sectionId", source = "section.id")
//	StudentHistoryDTO toDto(StudentHistory entity);
//
//	@Mapping(target = "student", ignore = true) // Handle in service
//	@Mapping(target = "enrollment", ignore = true) // Handle in service
//	@Mapping(target = "classInfo", ignore = true) // Handle in service
//	@Mapping(target = "section", ignore = true) // Handle in service
//	StudentHistory toEntity(StudentHistoryDTO dto);
//
//	List<StudentHistoryDTO> toStudentHistoryDtoList(List<StudentHistory> entities);
//
//	// ---------------- Update Mappings ----------------
//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "enrollments", ignore = true)
//	void updateStudentFromDto(StudentDto dto, @MappingTarget Student entity);
//
//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "sections", ignore = true)
//	void updateClassInfoFromDto(ClassInfoDTO dto, @MappingTarget ClassInfo entity);
//
//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "classEntity", ignore = true)
//	void updateSectionFromDto(SectionDTO dto, @MappingTarget Sections entity);
}
