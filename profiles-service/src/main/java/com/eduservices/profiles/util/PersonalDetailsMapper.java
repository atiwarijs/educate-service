package com.eduservices.profiles.util;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.profiles.entity.PersonalDetails;

@Mapper(componentModel = "spring")
public interface PersonalDetailsMapper {

	/**
	 * Converts a PersonalDetailsDto to PersonalDetails entity. Add
	 * specific @Mapping annotations if field names differ.
	 */
	// @Mapping(target = "differentFieldName", source = "fieldName")
	PersonalDetails toEntity(PersonalDetailsDto dto);

	/**
	 * Converts a PersonalDetails entity to PersonalDetailsDto. Add
	 * specific @Mapping annotations if field names differ.
	 */
	// @Mapping(target = "fieldName", source = "differentFieldName")
	PersonalDetailsDto toDto(PersonalDetails entity);
	
	/**
	 * Updates PersonalDetails entity from PersonalDetailsDto.
	 */
	void updateEntityFromDto(PersonalDetailsDto dto, @MappingTarget PersonalDetails entity);
}
