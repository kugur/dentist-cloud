package com.kolip.dentist.dentistservice.service;

import com.kolip.dentist.dentistservice.dto.DentistRequest;
import com.kolip.dentist.dentistservice.models.Dentist;

public interface DentistService {
    Dentist create(DentistRequest dentistRequest);
}
