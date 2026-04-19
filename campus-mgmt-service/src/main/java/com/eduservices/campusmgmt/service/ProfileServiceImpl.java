package com.eduservices.campusmgmt.service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eduservices.profile.dto.PersonalDetailsDto;
import com.eduservices.campusmgmt.exception.OperationFailedException;
import com.eduservices.campusmgmt.utils.HttpConnector;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    
    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

	@Autowired
	HttpConnector httpConnector;

	@Autowired
	RestTemplate restTemplate;

	@Value("${personal.base.url}")
	String profileApiUrl;

	@Override
	public List<PersonalDetailsDto> fetchAllPersonProfile() {
		return fetchAllProfiles();
	}

	@Override
	public Object fetchAllPersonProfileByPage(Integer page, Integer size, String[] sort) {
		return fetchAllProfilesByPage(page, size, sort);
	}

	@Override
	public PersonalDetailsDto createPersonProfile(PersonalDetailsDto person) {
		return processRequest(person, HttpMethod.POST, "created");
	}

	@Override
	public PersonalDetailsDto updatePersonProfile(PersonalDetailsDto person) {
		return processRequest(person, HttpMethod.PUT, "update");
	}

	@Override
	public PersonalDetailsDto getPersonProfileById(Long id) {
		HttpHeaders headers = getHeaders();
		HttpEntity<PersonalDetailsDto> request = new HttpEntity<>(headers);
		ResponseEntity<?> responseEntity = httpConnector.execute(this.profileApiUrl + "/personal/" + id, request,
				HttpMethod.GET, new PersonalDetailsDto());
		if (responseEntity.getStatusCode().value() == 200) {
			return (PersonalDetailsDto) responseEntity.getBody();
		} else {
			log.error("Profile fetch failed!, Error : {} {} ", responseEntity.getStatusCode().value(),
					responseEntity.getBody());
			throw new OperationFailedException("Profile fetch failed!");
		}
	}

	@Override
	public String deletePersonProfile(Long id) {
		HttpHeaders headers = getHeaders();
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<?> responseEntity = httpConnector.execute(this.profileApiUrl + "/personal/" + id, request,
				HttpMethod.DELETE, "");
		if (responseEntity.getStatusCode().value() == 200) {
			return (String) responseEntity.getBody();
		} else {
			log.error("Profile delete failed!, Error : {} {} ", responseEntity.getStatusCode().value(),
					responseEntity.getBody());
			throw new OperationFailedException("Profile delete failed!");
		}
	}

	private PersonalDetailsDto processRequest(PersonalDetailsDto person, HttpMethod httpMethod, String profileType) {
		try {
//			person.setUserId(userId);
			HttpHeaders headers = getHeaders();
			HttpEntity<PersonalDetailsDto> request = new HttpEntity<>(person, headers);
			ResponseEntity<?> responseEntity = httpConnector.execute(this.profileApiUrl + "/personal", request,
					httpMethod, person);
			if (responseEntity.getStatusCode().value() == 200 || responseEntity.getStatusCode().value() == 201) {
				PersonalDetailsDto responseDto = (PersonalDetailsDto) responseEntity.getBody();
				assert responseDto != null;
				log.info("{} profile {} successfully!", responseDto.getFirstName() + " " + responseDto.getLastName(),
						profileType);
				return responseDto;
			} else {
				log.error("Profile {} failed!, Error : {} {} ", responseEntity.getStatusCode().value(),
						responseEntity.getBody(), profileType);
				throw new OperationFailedException("Profile " + profileType + " failed!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new OperationFailedException("Profile " + profileType + " failed!, " + e.getMessage());
		}
	}

//	public String getCurrentUserId() {
//		JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
//				.getAuthentication();
//		Jwt jwt = (Jwt) authentication.getPrincipal();
//
//		return jwt.getSubject(); // Usually the UUID
////		return UUID.fromString(sub);
//	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Object fetchAllProfilesByPage(Integer page, Integer size, String[] sort) {
		try {
			String uriWithParams = uriWithParams = "?page=" + page + "&size=" + size + "&sort=" + sort[0] + "&sort="
					+ sort[1];
			ResponseEntity<?> responseEntity = restTemplate.exchange(
					new URI(this.profileApiUrl + "/personal" + uriWithParams), HttpMethod.GET, null, Object.class);
			if (responseEntity.getStatusCode().value() == 200 || responseEntity.getStatusCode().value() == 204) {
				return responseEntity.getBody();
			} else {
				log.error("All Profile fetch failed!, Error : {} {} ", responseEntity.getStatusCode().value(),
						responseEntity.getBody());
				throw new OperationFailedException("All Profile fetch failed!");
			}
		} catch (Exception e) {
			log.error("Execute failed!, Error: {} ", e.getMessage());
			throw new OperationFailedException(e.getMessage());
		}
	}

	private List<PersonalDetailsDto> fetchAllProfiles() {
		try {
			ResponseEntity<List<PersonalDetailsDto>> responseEntity = restTemplate.exchange(
					new URI(this.profileApiUrl + "/personal/list"), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<PersonalDetailsDto>>() {
					});
			if (responseEntity.getStatusCode().value() == 200 || responseEntity.getStatusCode().value() == 204) {
				return responseEntity.getBody();
			} else {
				log.error("fetchAllProfiles All Profile fetch failed!, Error : {} {} ",
						responseEntity.getStatusCode().value(), responseEntity.getBody());
				throw new OperationFailedException("All Profile fetch failed!");
			}
		} catch (Exception e) {
			log.error("Execute fetchAllProfiles failed!, Error: {} ", e.getMessage());
			throw new OperationFailedException(e.getMessage());
		}
	}

	/**
	 * Synchronizes user data between security service and profile service
	 */
	public void syncUserWithProfile(PersonalDetailsDto userDTO) {
		log.info("Syncing user with profile: {}", userDTO.getUserId());

		// Find if a profile exists for this user by searching based on userId
		PersonalDetailsDto existingProfile = findProfileByUserId(userDTO.getUserId());

		if (existingProfile != null) {
			log.info("User profile already exists for user: {}", userDTO.getUserId());
			// Update existing profile if needed
			updateExistingProfile(existingProfile, userDTO);
		} else {
			// Create new profile
			createNewUserProfile(userDTO);
		}
	}

	private PersonalDetailsDto findProfileByUserId(String userId) {
		try {
			// Call profile API to find profile by userId
			ResponseEntity<List<PersonalDetailsDto>> responseEntity = restTemplate.exchange(
					new URI(this.profileApiUrl + "/personal/findByUserId/" + userId), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<PersonalDetailsDto>>() {
					});

			if (responseEntity.getStatusCode().value() == 200 && responseEntity.getBody() != null
					&& !responseEntity.getBody().isEmpty()) {
				return responseEntity.getBody().get(0);
			}
			return null;
		} catch (Exception e) {
			log.error("Error finding profile by userId", e);
			return null;
		}
	}

	private void updateExistingProfile(PersonalDetailsDto existingProfile, PersonalDetailsDto userDTO) {
		// Update any fields that might have changed
		if (!existingProfile.getEmail().equals(userDTO.getEmail())) {
			existingProfile.setEmail(userDTO.getEmail());
			existingProfile.setUserId(userDTO.getUserId());
			// Update in profile service using the existing method
			try {
				updatePersonProfile(existingProfile);
				log.info("Updated profile email for user: {}", userDTO.getUsername());
			} catch (Exception e) {
				log.error("Error updating profile in profile service", e);
			}
		}
	}

	private void createNewUserProfile(PersonalDetailsDto userDTO) {
		// Create default personal details
//		PersonalDetailsDto personalDetails = createDefaultPersonalDetails(userDTO);

		try {
			// Use existing method to create personal profile
			PersonalDetailsDto createdProfile = createPersonProfile(userDTO);
			log.info("Created new profile for user: {}", userDTO.getUsername());
		} catch (Exception e) {
			log.error("Error creating profile in profile service", e);
			// Consider implementing retry logic or queue for failed operations
		}
	}

//	private PersonalDetailsDto createDefaultPersonalDetails(PersonalDetailsDto userDTO) {
//		return PersonalDetailsDto.builder().userId(userDTO.getUserId()).email(userDTO.getEmail())
//				.firstName(extractFirstName(userDTO.getUsername())).lastName(extractLastName(userDTO.getUsername())).build();
//	}

//	private String extractFirstName(String username) {
//		// Basic extraction logic - can be improved
//		if (username != null && username.contains(".")) {
//			return username.substring(0, username.indexOf('.')).toUpperCase();
//		}
//		return username;
//	}
//
//	private String extractLastName(String username) {
//		// Basic extraction logic - can be improved
//		if (username != null && username.contains(".")) {
//			return username.substring(username.indexOf('.') + 1).toUpperCase();
//		}
//		return "";
//	}

}
