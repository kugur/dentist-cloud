package com.kolip.dentist.dentistservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolip.dentist.dentistservice.constants.DentalSpecialization;
import com.kolip.dentist.dentistservice.dto.DentistRequest;
import com.kolip.dentist.dentistservice.models.Dentist;
import com.kolip.dentist.dentistservice.service.DentistService;
import com.kolip.dentistservice.basetest.AbstractTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DentistControllerTest extends AbstractTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private DentistService dentistService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup((new DentistController(dentistService))).build();
    }

    @Test
    public void createDentist_WithValidInput_ShouldCallUserService() throws Exception {
        //Initialize Test
        long dentistId = 1L;
        DentistRequest signUpRequest = generateDentistRequest();
        String requestBody = objectMapper.writeValueAsString(signUpRequest);
        when(dentistService.create(any())).thenAnswer((invocation) -> generateDentist(invocation.getArgument(0),
                                                                                      dentistId));

        //Run Test, Verify Result
        mockMvc.perform(post("/api/dentist").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dentistId))
                .andExpect(jsonPath("$.email").value(signUpRequest.getEmail()));
    }

    private DentistRequest generateDentistRequest() {
        return DentistRequest.builder()
                .email("ugur@mgail.com")
                .firstName("ugur")
                .lastName("soy isim")
                .dateBirth(Date.valueOf("1999-01-23"))
                .dentalSpecialization(DentalSpecialization.ORTHODONTICS)
                .build();
    }

    private Dentist generateDentist(DentistRequest dentistRequest, long id) {
        return Dentist.builder().email(dentistRequest.getEmail())
                .dentalSpecialization(dentistRequest.getDentalSpecialization())
                .phoneNumber(dentistRequest.getPhoneNumber())
                .graduatedUniversity(dentistRequest.getGraduatedUniversity())
                .firstName(dentistRequest.getFirstName())
                .lastName(dentistRequest.getLastName())
                .dateBirth(dentistRequest.getDateBirth())
                .id(id)
                .build();
    }

}