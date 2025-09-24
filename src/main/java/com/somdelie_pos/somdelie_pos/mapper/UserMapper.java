package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
        userDto.setDeletedAt(user.getDeletedAt());
        userDto.setLastLogin(user.getLastLogin());
        userDto.setStoreId(user.getStore()!=null? user.getStore().getId():null);
        userDto.setBranchId(user.getBranch()!=null? user.getBranch().getId():null);
        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        // Only set ID for existing entities (updates)
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }

        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        // Password should be handled separately for security
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(userDto.getDeletedAt());
        user.setLastLogin(userDto.getLastLogin());

        return user;
    }
}