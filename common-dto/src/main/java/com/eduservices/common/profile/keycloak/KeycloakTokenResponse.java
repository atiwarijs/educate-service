package com.eduservices.profile.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Keycloak token response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private Integer expiresIn;

	@JsonProperty("refresh_expires_in")
	private Integer refreshExpiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("id_token")
	private String idToken;

	@JsonProperty("not-before-policy")
	private Integer notBeforePolicy;

	@JsonProperty("session_state")
	private String sessionState;

	@JsonProperty("scope")
	private String scope;
}
