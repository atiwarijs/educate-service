package com.eduservices.security.mapper;

import com.eduservices.profile.dto.UserDTO;
import com.eduservices.security.entity.User;

public class UserMapper {

	public static UserDTO toDto(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
	}
}
