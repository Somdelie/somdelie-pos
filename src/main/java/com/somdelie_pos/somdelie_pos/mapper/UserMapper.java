package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setDeletedAt(user.getDeletedAt());
        dto.setLastLogin(user.getLastLogin());
        dto.setStoreId(user.getStore() != null ? user.getStore().getId() : null);
        dto.setBranchId(user.getBranch() != null ? user.getBranch().getId() : null);
        // Never set password on DTO outbound
        return dto;
    }

    /**
     * Creates a new User entity from DTO data (for create operations).
     * Does NOT set store/branch – those should be managed in services.
     * Does NOT hash password – handle hashing in service layer before save.
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        if (dto.getId() != null) {
            user.setId(dto.getId());
        }
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null) {
            // EXPECTATION: set a pre-hashed password OR hash outside before persisting.
            user.setPassword(dto.getPassword());
        }
        // Let @PrePersist / @PreUpdate or service layer handle timestamps
        user.setCreatedAt(dto.getCreatedAt()); // optional: remove if you prefer JPA only
        user.setUpdatedAt(dto.getUpdatedAt());
        user.setDeletedAt(dto.getDeletedAt());
        user.setLastLogin(dto.getLastLogin());
        return user;
    }

    /**
     * Partial update: apply only non-null fields from DTO to existing entity.
     * Does NOT touch password unless explicitly provided.
     */
    public static void updateEntity(User existing, UserDto dto) {
        if (existing == null || dto == null) return;

        if (dto.getFullName() != null) existing.setFullName(dto.getFullName());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getRole() != null) existing.setRole(dto.getRole());
        if (dto.getPassword() != null) {
            // Hash outside or ensure already hashed
            existing.setPassword(dto.getPassword());
        }

        // Timestamps: usually updatedAt handled automatically; only set if business case
        if (dto.getDeletedAt() != null) existing.setDeletedAt(dto.getDeletedAt());
        if (dto.getLastLogin() != null) existing.setLastLogin(dto.getLastLogin());
        // storeId / branchId not set here—those are managed through relation assignment in services
    }
}