package com.eduservices.profile.keycloak;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.eduservices.profile.config.FeignClientConfig;

/**
 * Feign client interface for Keycloak API
 */
@FeignClient(name = "keycloak", url = "${keycloak.auth-server-url}", configuration = FeignClientConfig.class)
public interface KeycloakClient {

	/**
	 * Get token from Keycloak
	 * 
	 * @param realm        Realm name
	 * @param tokenRequest Token request DTO containing form parameters
	 * @return Keycloak token response
	 */
	@PostMapping(value = "/realms/{realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	KeycloakTokenResponse getToken(@PathVariable("realm") String realm, @RequestBody TokenRequestDto tokenRequest);

	/**
	 * Get Keycloak users by username
	 * 
	 * @param realm      Realm name
	 * @param username   Username to search for
	 * @param authHeader Authorization header with bearer token
	 * @return List of user DTOs
	 */
	@GetMapping("/admin/realms/{realm}/users?username={username}")
	List<KeycloakUserDto> getUsersByUsername(@PathVariable("realm") String realm,
			@PathVariable("username") String username, @RequestHeader("Authorization") String authHeader);

	/**
	 * Get Keycloak user by ID
	 * 
	 * @param realm      Realm name
	 * @param userId     User ID
	 * @param authHeader Authorization header with bearer token
	 * @return User DTO
	 */
	@GetMapping("/admin/realms/{realm}/users/{userId}")
	KeycloakUserDto getUserById(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authHeader);

	/**
	 * Create Keycloak user
	 * 
	 * @param realm      Realm name
	 * @param userId     User ID
	 * @param userDto    User data to update
	 * @param authHeader Authorization header with bearer token
	 */
	@PostMapping("/admin/realms/{realm}/users")
	void createUser(@PathVariable("realm") String realm, @RequestBody KeycloakUserDto userDto,
			@RequestHeader("Authorization") String authHeader);

	/**
	 * Update Keycloak user
	 * 
	 * @param realm      Realm name
	 * @param userId     User ID
	 * @param userDto    User data to update
	 * @param authHeader Authorization header with bearer token
	 */
	@PutMapping("/admin/realms/{realm}/users/{userId}")
	void updateUser(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
			@RequestBody KeycloakUserDto userDto, @RequestHeader("Authorization") String authHeader);

	/**
	 * Reset user password
	 * 
	 * @param realm      Realm name
	 * @param userId     User ID
	 * @param credential Credential data
	 * @param authHeader Authorization header with bearer token
	 */
	@PutMapping("/admin/realms/{realm}/users/{userId}/reset-password")
	void resetPassword(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
			@RequestBody Map<String, Object> credential, @RequestHeader("Authorization") String authHeader);

	@GetMapping("/admin/realms/{realm}/roles/{roleName}")
	KeycloakRoleDto getRealmRole(@PathVariable("realm") String realm, @PathVariable("roleName") String roleName,
			@RequestHeader("Authorization") String authHeader);

	@PostMapping("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
	void assignRealmRoles(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
			@RequestBody List<KeycloakRoleDto> roles, @RequestHeader("Authorization") String authHeader);

	@PostMapping("/admin/realms/{realm}/roles")
	void createRealmRole(@PathVariable("realm") String realm, @RequestBody KeycloakRoleDto roleDto,
			@RequestHeader("Authorization") String authHeader);

	/**
	 * Delete a user from Keycloak
	 * 
	 * @param realm      The realm name
	 * @param userId     The ID of the user to delete
	 * @param authHeader The authentication header with bearer token
	 */
	@DeleteMapping("/admin/realms/{realm}/users/{userId}")
	void deleteUser(@PathVariable("realm") String realm, @PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authHeader);

	@GetMapping("/admin/realms/{realm}/users")
	List<KeycloakUserDto> getUsersByEmail(@PathVariable("realm") String realm, @RequestParam("email") String email,
			@RequestHeader("Authorization") String token);

}
