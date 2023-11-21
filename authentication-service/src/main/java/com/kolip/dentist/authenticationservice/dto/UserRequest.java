package com.kolip.dentist.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "email could not be empty!")
    private String email;
    private String username;
    @NotBlank(message = "password could not be empty!")
    private String password;
}
