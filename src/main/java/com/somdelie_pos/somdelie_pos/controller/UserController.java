package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.mapper.UserMapper;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() throws UserException {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable UUID id) throws UserException, Exception {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new Exception("User not found");
        }
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}