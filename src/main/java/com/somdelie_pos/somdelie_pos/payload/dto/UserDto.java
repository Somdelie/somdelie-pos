package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    private String phone;

    @NotNull(message = "Role is required")
    private UserRole role;

    // password is required for create; optional for update
    private String password;

    private UUID branchId;

    private UUID storeId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLogin;
}