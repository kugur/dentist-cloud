package com.kolip.dentist.authenticationservice.controller;

import com.kolip.dentist.authenticationservice.dto.UserInfo;
import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;
import com.kolip.dentist.authenticationservice.service.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @GetMapping
    public UserInfo getUserInfo(@RequestHeader("userInfo") String username, @RequestHeader("roles") String roles) {

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(username);
        userInfo.setRoles(List.of(roles.replace("[", "").replace("]", "").replace("SCOPE_", "").split(",")));
        return userInfo;
    }

    private String getEmail(Authentication authentication) {
        return ((CustomUser) authentication.getPrincipal()).getEmail();
    }
}
