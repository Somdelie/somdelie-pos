package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.domain.StoreStatus;
import com.somdelie_pos.somdelie_pos.modal.StoreContact;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreDto {

    private UUID id;

    private String brandName;

    private UserDto storeAdmin;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String description;

    private String storeType;

    private StoreStatus status;

    private StoreContact contact;

}