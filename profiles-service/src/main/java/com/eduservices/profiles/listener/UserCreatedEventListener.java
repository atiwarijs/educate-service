package com.eduservices.profiles.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.profiles.repo.PersonalRepository;
import com.eduservices.profiles.entity.PersonalDetails;
import com.eduservices.profiles.util.PersonalDetailsMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserCreatedEventListener {

	@Autowired
	private PersonalRepository personalRepository;

	@Autowired
	private PersonalDetailsMapper personalDetailsMapper;

	@KafkaListener(topics = "user.created", groupId = "user-service-group")
	public void handleUserCreated(PersonalDetailsDto userDTO) {
		System.out.println("Received user created event: " + userDTO.getUsername());
		try {
			PersonalDetails details = personalDetailsMapper.toEntity(userDTO);
			personalRepository.save(details);
			// Synchronize the user with profile service using the integrated method
//            profileService.syncUserWithProfile(userDTO);
		} catch (Exception e) {
//			log.error("Error processing user created event", e);
			// Consider implementing a dead letter queue or retry mechanism
		}
	}

//	@KafkaListener(topics = "user.updated", groupId = "user-service-group")
//	public void handleUserUpdated(PersonalDetailsDto userDTO) {
//		System.out.println("Received user updated event: " + userDTO.getUsername());
//		try {
//			// Synchronize the user with profile service using the integrated method
//			profileService.syncUserWithProfile(userDTO);
//		} catch (Exception e) {
//			log.error("Error processing user updated event", e);
//			// Consider implementing a dead letter queue or retry mechanism
//		}
//	}
}
