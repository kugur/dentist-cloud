package com.kolip.dentist.authenticationservice.controller;

import com.kolip.dentist.authenticationservice.service.CustomTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class LoginControllerTest {
    @MockBean
    CustomTokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(tokenService)).build();
    }

    @Test
    @WithMockUser(username = "ugur@gmail.com", roles = {"USER"})
    public void login_CallByAuthenticatedUser_ShouldSetTokenToResponse() throws Exception {
        //Initialize
        when(tokenService.createToken(any())).thenReturn("generated_token");

        //Run Test
        mockMvc.perform(post("/api/login")).andExpect(status().isOk());
    }
}