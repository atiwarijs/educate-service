package com.eduservices.profile.keycloak;

/**
 * DTO for user registration in Keycloak
 */
public class UserRegistrationDto {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean enabled;

	public UserRegistrationDto() {
		this.enabled = true;
	}

	public static UserRegistrationDto from(String username, String email, String firstName, String lastName) {
		UserRegistrationDto dto = new UserRegistrationDto();
		dto.setUsername(username);
		dto.setEmail(email);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		return dto;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
