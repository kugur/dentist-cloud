package com.kolip.dentist.dentistservice.models;

import com.kolip.dentist.dentistservice.constants.DentalSpecialization;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dentist {
    private long id;
    private String firstName;
    private String lastName;
    private Date dateBirth;
    private String phoneNumber;
    private String email;
    private DentalSpecialization dentalSpecialization;
    private String graduatedUniversity;
}
