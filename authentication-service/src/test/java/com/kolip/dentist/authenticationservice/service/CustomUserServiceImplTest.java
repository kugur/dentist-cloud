package com.kolip.dentist.authenticationservice.service;

import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;
import com.kolip.dentist.authenticationservice.repository.CustomUserRepository;
import com.kolip.dentist.authenticationservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CustomUserServiceImpl.class, CustomUserRepository.class})
class CustomUserServiceImplTest {

    private CustomUserService instanceUnderTest;

    @MockBean
    private CustomUserRepository customUserRepository;
    @MockBean
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        instanceUnderTest = new CustomUserServiceImpl(customUserRepository, roleRepository);
    }

    @Test
    public void create_WithValidInput_ShouldCallRepository() {
        //Initialize
        UserRequest userRequest =
                UserRequest.builder().email("deneme@gmail.com").username("deneme").password("1234").build();

        when(customUserRepository.save(any())).thenReturn(
                CustomUser.builder().id(11L).username(userRequest.getUsername()).email(userRequest.getEmail()).build());

        //Run Test
        UserResponse user = instanceUnderTest.create(userRequest);

        //Verify Result
        assertNotNull(user.getId());
        assertEquals(userRequest.getEmail(), user.getEmail());
        verify(customUserRepository).save(any());
    }

    @Test
    public void create_WithNotValidUserRequest_ShouldReturnErrorMessage() {
        //Initialize
        UserRequest userRequest = UserRequest.builder().email("deneme@gmail.com").build();

        //Run Test
        UserResponse userResponse = instanceUnderTest.create(userRequest);

        //Verify Result
        verify(customUserRepository, times(0)).save(any());
        assertNotNull(userResponse.getHttpError());
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getHttpError().getHttpStatusCode());
    }

    @Test
    public void create_WithPassword_PasswordShouldBeEncrypt() {
        //Initialize
        UserRequest userRequest = UserRequest.builder().email("deneme@gmail.com").password("plainPassword").build();
        ArgumentCaptor<CustomUser> repositoryCapture = ArgumentCaptor.forClass(CustomUser.class);
        when(customUserRepository.save(repositoryCapture.capture())).thenReturn(CustomUser.builder()
                .email(userRequest.getEmail())
                .build());

        //Run Test
        instanceUnderTest.create(userRequest);

        //Verify Result
        CustomUser toBeSavedUser = repositoryCapture.getValue();
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        assertTrue(passwordEncoder.matches(userRequest.getPassword(), toBeSavedUser.getPassword()));
    }
}