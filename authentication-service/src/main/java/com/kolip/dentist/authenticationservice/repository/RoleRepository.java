package com.kolip.dentist.authenticationservice.repository;

import com.kolip.dentist.authenticationservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleUser);
}
