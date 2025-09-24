package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BranchDTO {
    private UUID id;

    private String name;

    private String address;

    private String phone;

    private String email;

    private List<String> workingDays;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private StoreDto store;

    private UUID storeId;

    private UserDto manager;

}
