package com.shopverse.backend.mapper;

import com.shopverse.backend.dto.UserDTO;
import com.shopverse.backend.models.User;

public class UserMapper {

	public static UserDTO toDTO(User user) {
		if (user == null) {
			return null;
		}

		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setUserName(user.getUserName());
		return dto;
	}
}
