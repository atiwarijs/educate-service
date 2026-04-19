package com.eduservices.profile.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for making token requests to Keycloak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {
	private String grant_type;
	private String client_id;
	private String client_secret;
	private String username;
	private String password;
	private String refresh_token;

	/**
	 * Create a token request DTO for password grant type
	 * 
	 * @param clientId     Client ID
	 * @param clientSecret Client secret
	 * @param username     Username
	 * @param password     Password
	 * @return TokenRequestDto configured for password grant
	 */
	public static TokenRequestDto forPasswordGrant(String clientId, String clientSecret, String username,
			String password) {
		return TokenRequestDto.builder().grant_type("password").client_id(clientId).client_secret(clientSecret)
				.username(username).password(password).build();
	}

	/**
	 * Create a token request DTO for refresh token grant type
	 * 
	 * @param clientId     Client ID
	 * @param clientSecret Client secret
	 * @param refreshToken Refresh token
	 * @return TokenRequestDto configured for refresh token grant
	 */
	public static TokenRequestDto forRefreshToken(String clientId, String clientSecret, String refreshToken) {
		return TokenRequestDto.builder().grant_type("refresh_token").client_id(clientId).client_secret(clientSecret)
				.refresh_token(refreshToken).build();
	}

	/**
	 * Create a token request DTO for client credentials grant type
	 * 
	 * @param clientId     Client ID
	 * @param clientSecret Client secret
	 * @return TokenRequestDto configured for client credentials grant
	 */
	public static TokenRequestDto forClientCredentials(String clientId, String clientSecret) {
		return TokenRequestDto.builder().grant_type("client_credentials").client_id(clientId)
				.client_secret(clientSecret).build();
	}
}
