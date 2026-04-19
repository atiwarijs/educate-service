package com.eduservices.profile.keycloak;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Keycloak user information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeycloakUserDto {
	private String id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean enabled;
	private Boolean emailVerified;
	private Map<String, List<String>> attributes;
	private List<CredentialDto> credentials;
//	private List<String> requiredActions;
}
