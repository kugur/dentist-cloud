package com.kolip.dentist.patientservice.controller;

import com.kolip.dentist.patientservice.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@Log4j2
public class PatientController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PatientResponse> getAllPatients() {
        log.info("Patient getAll has been called.");
        return List.of(PatientResponse.builder().id("123").name("deneme isim").build());
    }
}
