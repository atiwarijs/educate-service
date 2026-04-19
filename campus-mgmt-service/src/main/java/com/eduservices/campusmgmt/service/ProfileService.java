package com.eduservices.campusmgmt.service;

import java.util.List;

import com.eduservices.profile.dto.PersonalDetailsDto;

public interface ProfileService {

    List<PersonalDetailsDto> fetchAllPersonProfile();

    Object fetchAllPersonProfileByPage(Integer page, Integer size, String[] sort);

    PersonalDetailsDto createPersonProfile(PersonalDetailsDto person);

    PersonalDetailsDto updatePersonProfile(PersonalDetailsDto person);

    PersonalDetailsDto getPersonProfileById(Long id);

    String deletePersonProfile(Long id);
}
