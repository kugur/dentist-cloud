package com.kolip.dentist.authenticationservice.controller;

import com.kolip.dentist.authenticationservice.service.CustomTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class JwtTokenController {

    private final CustomTokenService customTokenService;

    public JwtTokenController(CustomTokenService customTokenService) {
        this.customTokenService = customTokenService;
    }

    @PostMapping
    public String token(Authentication authentication) {
        return customTokenService.createToken(authentication);
    }


    @GetMapping
    public String getToken() {

        return "tokeni aldin tebrik";
    }
}
