package com.kolip.dentist.patientservice.controller;

import com.kolip.dentist.patientservice.dto.PatientRequest;
import com.kolip.dentist.patientservice.dto.PatientResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PatientResponse> getAllPatients() {
        log.info("Patient getAll has been called.");
        return List.of(PatientResponse.builder().id("123").name("deme isim").build());
    }

    @PostMapping
    public PatientResponse createPatient(PatientRequest patientRequest) {
        return PatientResponse.builder().id("12").name("deneme patient").build();
    }
}
