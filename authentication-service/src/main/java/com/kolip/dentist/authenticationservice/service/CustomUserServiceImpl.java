package com.kolip.dentist.authenticationservice.service;

import com.kolip.dentist.authenticationservice.constants.Roles;
import com.kolip.dentist.authenticationservice.dto.HttpError;
import com.kolip.dentist.authenticationservice.dto.UserRequest;
import com.kolip.dentist.authenticationservice.dto.UserResponse;
import com.kolip.dentist.authenticationservice.model.CustomUser;
import com.kolip.dentist.authenticationservice.model.Role;
import com.kolip.dentist.authenticationservice.repository.CustomUserRepository;
import com.kolip.dentist.authenticationservice.repository.RoleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ugur.kolip on 20/11/2023.
 * CRUD for customUser.
 */
@Service
public class CustomUserServiceImpl implements CustomUserService {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public CustomUserServiceImpl(CustomUserRepository customUserRepository, RoleRepository roleRepository) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.roleRepository = roleRepository;
    }

    @Override
    public UserResponse create(UserRequest userRequest) {
        return create(userRequest, true, Roles.USER);
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest userRequest, boolean shouldValidate, Roles role) {
        Set<ConstraintViolation<UserRequest>> errors = validateInput(userRequest);
        if (shouldValidate && !errors.isEmpty()) {
            return UserResponse.builder()
                    .httpError(HttpError.builder()
                                       .httpStatusCode(HttpStatus.BAD_REQUEST)
                                       .errors(errors.stream()
                                                       .map(ConstraintViolation::getMessage)
                                                       .collect(Collectors.toList()))
                                       .build()).build();
        }

        Role updatedRole = roleRepository.findByName(role.getRoleName());
        CustomUser beCreatedUser = convert(userRequest);
        beCreatedUser.setRoles(Set.of(updatedRole));
        CustomUser savedUser = customUserRepository.save(beCreatedUser);

        return convert(savedUser);
    }

    @Override
    public CustomUser findByEmail(String username) {
        CustomUser user = customUserRepository.findByEmail(username);
        if ("uur@gmail.com".equals(username)) {
            user.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }
        return user;
    }

    private CustomUser convert(UserRequest userRequest) {
        return CustomUser.builder().email(userRequest.getEmail())
                .password(userRequest.getPassword() != null ? passwordEncoder.encode(userRequest.getPassword()) : "")
                .username(userRequest.getUsername())
                .build();
    }

    private UserResponse convert(CustomUser user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .authorities(user.getAuthorities())
                .build();
    }

    private Set<ConstraintViolation<UserRequest>> validateInput(UserRequest input) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        return validator.validate(input);
    }
}
