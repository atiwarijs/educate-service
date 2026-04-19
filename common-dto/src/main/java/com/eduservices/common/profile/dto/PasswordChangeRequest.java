package com.eduservices.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {
	private String userId;
	private String userName;
	private String newPassword;
	private String oldPassword;
   
	// Getters and setters
}
