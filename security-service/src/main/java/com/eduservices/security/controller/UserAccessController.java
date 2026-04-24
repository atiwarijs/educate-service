package com.eduservices.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.profile.dto.UserDTO;
import com.eduservices.security.entity.User;
import com.eduservices.security.exception.AuthorizationException;
import com.eduservices.security.mapper.UserMapper;
import com.eduservices.security.service.AuthService;
import com.eduservices.security.service.UserService;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserAccessController {

	private static final Logger logger = LoggerFactory.getLogger(UserAccessController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/userinfo/{username}/{email}")
	public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Jwt jwt, @PathVariable String username,
			@PathVariable String email) throws AuthException {
		PersonalDetailsDto personalDto = null;
		String sub = jwt.getSubject(); // UUID from Keycloak
		String usernameKeycloak = jwt.getClaimAsString("preferred_username");
		String emailKeycloak = jwt.getClaimAsString("email");

		if (usernameKeycloak.equals(username) && emailKeycloak.equals(email)) {
			personalDto = userService.fetchUserInfoFromProfileApi(sub);
			return ResponseEntity.ok(personalDto);
		} else {
			throw new AuthorizationException("You are not authorized to get user details.");
		}
	}

	@GetMapping("/email-check/{email}")
	public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
		logger.info("Checking if email exists: " + email);
		boolean exists = userService.checkIfEmailExists(email);
		return ResponseEntity.ok(exists);
	}
}
