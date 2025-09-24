package com.somdelie_pos.somdelie_pos.payload.response;

import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserDto user;

}
