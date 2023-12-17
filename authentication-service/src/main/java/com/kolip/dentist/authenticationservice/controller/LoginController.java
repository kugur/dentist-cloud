package com.kolip.dentist.authenticationservice.controller;

import com.kolip.dentist.authenticationservice.dto.UserInfo;
import com.kolip.dentist.authenticationservice.model.CustomUser;
import com.kolip.dentist.authenticationservice.service.CustomTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final CustomTokenService tokenService;

    public LoginController(CustomTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public UserInfo login(Authentication authentication, HttpServletResponse response) {
        String authenticatedEmail = getEmail(authentication);
        String jwtToken =
                URLEncoder.encode("Bearer " + tokenService.createToken(authentication), StandardCharsets.UTF_8);
        Cookie tokenCookie = new Cookie("Authorization", jwtToken);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
//        response.sendRedirect("http://localhost:3000/");
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(authenticatedEmail);
        userInfo.setRoles(authentication.getAuthorities()
                                  .stream()
                                  .map(GrantedAuthority::getAuthority)
                                  .collect(
                                          Collectors.toList()));
        return userInfo;
    }

    private String getEmail(Authentication authentication) {
        return ((CustomUser)authentication.getPrincipal()).getEmail();
    }
}
