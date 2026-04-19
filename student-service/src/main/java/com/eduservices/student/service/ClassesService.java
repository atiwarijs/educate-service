package com.eduservices.student.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduservices.student.dto.ClassInfoDTO;
import com.eduservices.student.dto.SectionDTO;
import com.eduservices.student.entity.ClassInfo;
import com.eduservices.student.entity.Sections;
import com.eduservices.student.mapper.ClassMapper;
import com.eduservices.student.repo.ClassInfoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClassesService {

//	private static final Logger log = LoggerFactory.getLogger(ClassesService.class);

	@Autowired
	private ClassInfoRepository classRepository;

	@Transactional
	public ClassInfoDTO createClassWithSections(ClassInfoDTO request) {
		log.info("Processing create/update for className={}", request.getClassName());

		Optional<ClassInfo> existingClassOpt = classRepository.findByClassName(request.getClassName());

		ClassInfo classInfo;
		if (existingClassOpt.isPresent()) {
			// Update existing class
			classInfo = existingClassOpt.get();
			log.info("Updating existing ClassInfo with id={}", classInfo.getId());

			classInfo.setAcademicYear(request.getAcademicYear());
			classInfo.setStatus(request.getStatus());
			classInfo.setActive(true);

			// Map existing sections
			Map<String, Sections> existingSectionsMap = classInfo.getSections().stream()
					.collect(Collectors.toMap(Sections::getSectionName, s -> s));

			for (SectionDTO secReq : request.getSections()) {
				if (existingSectionsMap.containsKey(secReq.getSectionName())) {
					// Update existing section
					Sections existingSection = existingSectionsMap.get(secReq.getSectionName());
					existingSection.setStrength(secReq.getStrength());
					log.info("Updated section={} with new strength={}", secReq.getSectionName(), secReq.getStrength());
				} else {
					// Add new section (via ManualMapper)
					Sections newSection = ClassMapper.toSectionEntity(secReq);
					newSection.setClassEntity(classInfo);
					classInfo.getSections().add(newSection);
					log.info("Added new section={} with strength={}", secReq.getSectionName(), secReq.getStrength());
				}
			}

			// (Optional) Remove sections that are not in request
			classInfo.getSections().removeIf(sec -> request.getSections().stream()
					.noneMatch(r -> r.getSectionName().equals(sec.getSectionName())));

		} else {
			// Create new class (via ManualMapper)
			log.info("Creating new ClassInfo with name={}", request.getClassName());
			classInfo = ClassMapper.toClassInfoEntity(request);
			classInfo.setActive(true);

			// Ensure bidirectional link
			if (classInfo.getSections() != null) {
				classInfo.getSections().forEach(sec -> sec.setClassEntity(classInfo));
			}
		}

		ClassInfo saved = classRepository.save(classInfo);
		log.info("ClassInfo with id={} successfully persisted", saved.getId());

		return ClassMapper.toClassInfoDTO(saved);
	}

	@Transactional
	public ClassInfoDTO updateClassWithSections(ClassInfoDTO request) {
		log.info("Updating ClassInfo with id={} and name={}", request.getId(), request.getClassName());

		ClassInfo classInfo = classRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Class not found with id=" + request.getId()));

		// Update class details
		classInfo.setClassName(request.getClassName());
		classInfo.setAcademicYear(request.getAcademicYear());
		classInfo.setStatus(request.getStatus());
		classInfo.setActive(true);

		// Map existing sections
		Map<String, Sections> existingSectionsMap = classInfo.getSections().stream()
				.collect(Collectors.toMap(Sections::getSectionName, s -> s));

		// Update or add sections
		for (SectionDTO secReq : request.getSections()) {
			if (existingSectionsMap.containsKey(secReq.getSectionName())) {
				Sections existingSection = existingSectionsMap.get(secReq.getSectionName());
				log.info("Updating section={} with strength={}", secReq.getSectionName(), secReq.getStrength());
				existingSection.setStrength(secReq.getStrength());
			} else {
				Sections newSection = ClassMapper.toSectionEntity(secReq);
				newSection.setClassEntity(classInfo);
				log.info("Adding new section={} with strength={}", secReq.getSectionName(), secReq.getStrength());
				classInfo.getSections().add(newSection);
			}
		}

		// (Optional) Remove sections not in request
		classInfo.getSections().removeIf(
				sec -> request.getSections().stream().noneMatch(r -> r.getSectionName().equals(sec.getSectionName())));

		ClassInfo saved = classRepository.save(classInfo);
		log.info("ClassInfo with id={} successfully updated", saved.getId());

		return ClassMapper.toClassInfoDTO(saved);
	}

}
