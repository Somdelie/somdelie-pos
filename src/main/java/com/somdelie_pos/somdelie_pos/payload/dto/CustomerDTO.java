package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
}
