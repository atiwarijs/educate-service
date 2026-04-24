package com.eduservices.profile.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for credential operations (password resets)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDto {
	private String type;
	private String value;
	private boolean temporary;
}
