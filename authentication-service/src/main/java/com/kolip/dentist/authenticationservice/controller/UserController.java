package com.kolip.dentist.authenticationservice.controller;

import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.service.CustomUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final CustomUserService customUserService;

    public UserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = customUserService.create(userRequest);
        if (createdUser.getHttpError() != null) {
            return ResponseEntity.status(createdUser.getHttpError().getHttpStatusCode()).body(createdUser);
        }
        return ResponseEntity.ok(createdUser);
    }
}
