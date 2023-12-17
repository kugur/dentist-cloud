package com.kolip.dentist.authenticationservice.dto;

import com.kolip.dentist.authenticationservice.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    String email;
    List<String> roles;
}
