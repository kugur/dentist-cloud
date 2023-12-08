package com.kolip.dentist.authenticationservice.service;

import com.kolip.dentist.authenticationservice.constants.Roles;
import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;

public interface CustomUserService {
    UserResponse create(UserRequest userRequest);

    UserResponse create(UserRequest userRequest, boolean shouldValidate, Roles role);

    CustomUser findByEmail(String email);
}
