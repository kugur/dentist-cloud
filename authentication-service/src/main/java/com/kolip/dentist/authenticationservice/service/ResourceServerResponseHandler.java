package com.kolip.dentist.authenticationservice.service;

import com.kolip.dentist.authenticationservice.constants.Roles;
import com.kolip.dentist.authenticationservice.dto.SimpleAuthentication;
import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Qualifier("customSuccessHandler")
@Slf4j
public class ResourceServerResponseHandler implements AuthenticationSuccessHandler {

    private final CustomTokenService tokenService;
    private final CustomUserService userService;

    public ResourceServerResponseHandler(CustomTokenService tokenService, CustomUserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("on success handler on oauth2Login :: " + authentication.toString());
        String authenticatedEmail = getEmail(authentication);
        Authentication currentAuthentication = createAuthentication(authenticatedEmail);
        String jwtToken =
                URLEncoder.encode("Bearer " + tokenService.createToken(currentAuthentication), StandardCharsets.UTF_8);
        Cookie tokenCookie = new Cookie("Authorization", jwtToken);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
        response.sendRedirect("http://localhost:3000/");
    }

    private String getEmail(Authentication auth2AuthenticationToken) {
        if (auth2AuthenticationToken instanceof OAuth2AuthenticationToken) {
            return ((OidcUser) auth2AuthenticationToken.getPrincipal()).getClaim("email");
        }
        return null;
    }

    private SimpleAuthentication createAuthentication(String email) {
        CustomUser user = userService.findByEmail(email);
        SimpleAuthentication simpleAuthentication = new SimpleAuthentication();

        if (user == null) {
            UserRequest signUpUserWithSocialLogin = UserRequest.builder().email(email).username(email).build();
            UserResponse createdUser = userService.create(signUpUserWithSocialLogin, false, Roles.USER);
            simpleAuthentication.setName(createdUser.getEmail());
            simpleAuthentication.setAuthorities(createdUser.getAuthorities());

        } else {
            simpleAuthentication.setName(user.getUsername());
        }

        return simpleAuthentication;
    }
}
