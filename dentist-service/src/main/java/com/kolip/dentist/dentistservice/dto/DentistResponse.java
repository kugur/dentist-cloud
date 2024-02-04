package com.kolip.dentist.dentistservice.dto;

import com.kolip.dentist.dentistservice.constants.DentalSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DentistResponse {
    private long id;
    private String firstName;
    private String lastName;
    private Date dateBirth;
    private String phoneNumber;
    private String email;
    private DentalSpecialization dentalSpecialization;
    private String graduatedUniversity;
}
