package com.kolip.dentist.dentistservice.repository;

import com.kolip.dentist.dentistservice.models.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
}
