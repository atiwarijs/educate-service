package com.eduservices.campusmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.campusmgmt.service.ProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Person profiles", description = "This is profile details apis")
@RestController
@RequestMapping("/profile")
public class ProfileController  {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/list")
    public ResponseEntity<?> fetchALlPersonProfile(){
        List<PersonalDetailsDto> profileList = profileService.fetchAllPersonProfile();
        return new ResponseEntity<>(profileList, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> fetchALlPersonProfileByPage(
            @RequestParam(name = "page", defaultValue = "1" , required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name="sort", defaultValue = "id,asc", required = false) String[] sort
    ){
        Object profilePagination = profileService.fetchAllPersonProfileByPage(page, size, sort);
        return new ResponseEntity<>(profilePagination, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchPersonProfile(@PathVariable Long id){
        PersonalDetailsDto responseDto = profileService.getPersonProfileById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createProfile(@Valid @RequestBody PersonalDetailsDto dto, @AuthenticationPrincipal Jwt jwt){
    	 String userId = jwt.getSubject();
         System.out.println("User ID from JWT: " + userId);
        PersonalDetailsDto responseDto = profileService.createPersonProfile(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> updateProfile(@Valid @RequestBody PersonalDetailsDto dto){
        PersonalDetailsDto responseDto = profileService.updatePersonProfile(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonProfile(@PathVariable Long id){
        String response = profileService.deletePersonProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
