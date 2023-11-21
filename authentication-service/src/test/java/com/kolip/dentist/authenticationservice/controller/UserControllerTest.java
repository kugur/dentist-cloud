package com.kolip.dentist.authenticationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolip.dentist.authenticationservice.basetest.AbstractTest;
import com.kolip.dentist.authenticationservice.dto.HttpError;
import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.service.CustomUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class UserControllerTest extends AbstractTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CustomUserService customUserService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(customUserService)).build();
    }

    @Test
    public void createUser_WithValidInput_ShouldCallUserService() throws Exception {
        //Initialize Test
        String inputEmail = "deneme@gmail.com";
        UserRequest userRequest = UserRequest.builder().email(inputEmail).password("1234").build();
        String requestBody = objectMapper.writeValueAsString(userRequest);
        when(customUserService.create(eq(userRequest))).thenReturn(
                UserResponse.builder().id(11L).email(inputEmail).build());

        //Run Test
        mockMvc.perform(post("/api/user").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.email").value(inputEmail));

        //verify result
        verify(customUserService).create(any(UserRequest.class));
    }

    @Test
    public void createUser_ServiceReturnNotEmptyError_ShouldSetHttpCode() throws Exception {
        //Initialize
        UserRequest userRequest = UserRequest.builder().email("email.adresi").build();
        when(customUserService.create(any())).thenReturn(UserResponse.builder()
                .httpError(HttpError.builder()
                        .errorMessage("invalid parameters")
                        .httpStatusCode(HttpStatus.BAD_REQUEST)
                        .errors(List.of("password can not be empty"))
                        .build())
                .build());

        //Run Test
        mockMvc.perform(post("/api/user").content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}