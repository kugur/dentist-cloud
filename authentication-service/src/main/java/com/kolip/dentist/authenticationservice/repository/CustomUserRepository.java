package com.kolip.dentist.authenticationservice.repository;

import com.kolip.dentist.authenticationservice.model.CustomUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByEmail(String username);
}
