package com.kolip.dentist.authenticationservice.service;

import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;

public interface CustomUserService {
    UserResponse create(UserRequest userRequest);

    CustomUser findByEmail(String email);
}
