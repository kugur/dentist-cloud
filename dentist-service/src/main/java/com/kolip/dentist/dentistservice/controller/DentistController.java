package com.kolip.dentist.dentistservice.controller;

import com.kolip.dentist.dentistservice.dto.DentistRequest;
import com.kolip.dentist.dentistservice.dto.DentistResponse;
import com.kolip.dentist.dentistservice.exceptions.InvalidRequestException;
import com.kolip.dentist.dentistservice.models.Dentist;
import com.kolip.dentist.dentistservice.service.DentistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/dentist")
public class DentistController {

    private final DentistService dentistService;

    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @PostMapping
    public ResponseEntity<DentistResponse> create(@RequestBody DentistRequest dentistRequest) {
        Dentist createdDentist;
        try {
            createdDentist = dentistService.create(dentistRequest);
        } catch (InvalidRequestException ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(DentistResponse.builder().id(createdDentist.getId())
                                         .email(createdDentist.getEmail())
                                         .firstName(createdDentist.getFirstName())
                                         .lastName(createdDentist.getLastName())
                                         .dentalSpecialization(createdDentist.getDentalSpecialization()).build());
    }
}
