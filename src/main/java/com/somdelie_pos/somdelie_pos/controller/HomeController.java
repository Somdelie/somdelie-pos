package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> home() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Welcome to Somdelie pos system");
        return ResponseEntity.ok(apiResponse);
    }
}
