package com.eduservices.registrations.controller;

import com.eduservices.registrations.dto.RegistrationDto;
import com.eduservices.registrations.dto.SearchDto;
import com.eduservices.registrations.entity.RegistrationEntity;
import com.eduservices.registrations.service.RegistrationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.eduservices.controller.GenericController;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Registrations", description = "New Registrations details apis")
@RestController
@RequestMapping("/registrations")
public class RegistrationController extends GenericController<RegistrationDto, RegistrationEntity, Long> {

    @Autowired
    RegistrationService registrationService;

    protected RegistrationController(GenericRepository<RegistrationEntity, Long> genericRepository, ModelMapper mapper) {
        super(genericRepository, mapper, RegistrationDto.class, RegistrationEntity.class);
    }


    @Override
    @PostMapping()
    public ResponseEntity<RegistrationDto> save(@Valid @RequestBody RegistrationDto dto) {
        return super.save(registrationService.newRegistration(dto));
    }

    @PostMapping("/search")
    public List<RegistrationDto> search(@RequestBody SearchDto searchPayload){
        return registrationService.search(searchPayload);
    }

    @PostMapping("/generate")
    public String generateRegistrationNumber(@RequestBody SearchDto dto){
        return registrationService.generateRegistrationNumber(dto);
    }
}
