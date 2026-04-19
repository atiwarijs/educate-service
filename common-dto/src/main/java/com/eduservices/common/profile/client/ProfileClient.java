package com.eduservices.profile.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eduservices.profile.dto.PersonalDetailsDto;

@FeignClient(name = "profile-service", url = "${profile.api.url}")
public interface ProfileClient {

	@GetMapping("/personal/findByUserId/{userId}")
	PersonalDetailsDto getUserById(@PathVariable("userId") String userId);
}
