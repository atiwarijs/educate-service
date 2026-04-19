package com.eduservices.campusmgmt.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.profile.dto.UserDTO;
import com.eduservices.campusmgmt.service.ProfileServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Component	
public class UserCreatedEventListener {
	
    private static final Logger log = LoggerFactory.getLogger(UserCreatedEventListener.class);
	
	@Autowired
    private ProfileServiceImpl profileService;

	@KafkaListener(topics = "user.created", groupId = "user-service-group")
	public void handleUserCreated(PersonalDetailsDto userDTO) {
		System.out.println("Received user created event: " + userDTO.getUsername());
		try {
            // Synchronize the user with profile service using the integrated method
            profileService.syncUserWithProfile(userDTO);
        } catch (Exception e) {
            log.error("Error processing user created event", e);
            // Consider implementing a dead letter queue or retry mechanism
        }
	}
	
	@KafkaListener(topics = "user.updated", groupId = "user-service-group")
	public void handleUserUpdated(PersonalDetailsDto userDTO) {
		System.out.println("Received user updated event: " + userDTO.getUsername());
		try {
            // Synchronize the user with profile service using the integrated method
            profileService.syncUserWithProfile(userDTO);
        } catch (Exception e) {
            log.error("Error processing user updated event", e);
            // Consider implementing a dead letter queue or retry mechanism
        }
	}
}
