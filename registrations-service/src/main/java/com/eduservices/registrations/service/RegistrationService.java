package com.eduservices.registrations.service;

import com.eduservices.registrations.dto.RegistrationDto;
import com.eduservices.registrations.dto.SearchDto;

import java.util.List;

public interface RegistrationService {

    RegistrationDto newRegistration(RegistrationDto dto);

    List<RegistrationDto> search(SearchDto searchPayload);

    String generateRegistrationNumber(SearchDto dto);
}
