package com.kolip.dentist.dentistservice.service;

import com.kolip.dentist.dentistservice.dto.DentistRequest;
import com.kolip.dentist.dentistservice.exceptions.InvalidRequestException;
import com.kolip.dentist.dentistservice.models.Dentist;
import com.kolip.dentist.dentistservice.repository.DentistRepository;
import org.springframework.stereotype.Service;

/**
 * Created by ugur.kolip on 26/01/2024.
 * <p>
 * Dentist Service for create, read, update, delete
 */
@Service
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;

    public DentistServiceImpl(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @Override
    public Dentist create(DentistRequest dentistRequest) {
        if (dentistRequest == null) {
            throw new InvalidRequestException("Dentist Request could not be null");
        }
        Dentist toBeCreatedDentist = Dentist.builder().email(dentistRequest.getEmail())
                .dateBirth(dentistRequest.getDateBirth())
                .dentalSpecialization(dentistRequest.getDentalSpecialization())
                .firstName(dentistRequest.getFirstName())
                .lastName(dentistRequest.getLastName())
                .phoneNumber(dentistRequest.getPhoneNumber())
                .graduatedUniversity(dentistRequest.getGraduatedUniversity())
                .build();

        return dentistRepository.save(toBeCreatedDentist);
    }
}
