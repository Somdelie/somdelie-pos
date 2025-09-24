package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.domain.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String fullName;

    private String email;

    private String phone;

    private UserRole role;

    private String password;

    private UUID branchId;

    private UUID storeId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLogin;
}