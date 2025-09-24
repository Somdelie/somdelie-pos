package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;
import com.somdelie_pos.somdelie_pos.payload.response.AuthResponse;

public interface AuthService {


    AuthResponse signup(UserDto userDto) throws UserException;
    AuthResponse login(UserDto userDto) throws UserException;
}
