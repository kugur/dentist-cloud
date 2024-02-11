package com.kolip.dentist.dentistservice.service;

import com.kolip.dentist.dentistservice.constants.DentalSpecialization;
import com.kolip.dentist.dentistservice.dto.DentistRequest;
import com.kolip.dentist.dentistservice.exceptions.InvalidRequestException;
import com.kolip.dentist.dentistservice.models.Dentist;
import com.kolip.dentist.dentistservice.repository.DentistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {DentistServiceImpl.class})
class DentistServiceImplTest {

    private DentistServiceImpl dentistService;

    @MockBean
    private DentistRepository dentistRepository;

    @BeforeEach
    public void setup() {
        dentistService = new DentistServiceImpl(dentistRepository);
    }

    @Test
    public void create_WithValidInput_ShouldCallRepository() {
        //Initialize
        long id = 22L;
        DentistRequest dentistRequest = createDentistRequest();
        when(dentistRepository.save(any())).thenAnswer(invocationOnMock -> {
            Dentist toBeCreatedDentist = invocationOnMock.getArgument(0);
            toBeCreatedDentist.setId(id);
            return toBeCreatedDentist;
        });

        //Run Test
        Dentist createdDentist = dentistService.create(dentistRequest);

        //Verify Result
        assertEquals(dentistRequest.getEmail(), createdDentist.getEmail());
        assertEquals(dentistRequest.getFirstName(), createdDentist.getFirstName());
        assertEquals(dentistRequest.getLastName(), createdDentist.getLastName());
        assertEquals(dentistRequest.getDateBirth(), createdDentist.getDateBirth());
        assertEquals(dentistRequest.getGraduatedUniversity(), createdDentist.getGraduatedUniversity());
        assertEquals(dentistRequest.getPhoneNumber(), createdDentist.getPhoneNumber());
        assertEquals(dentistRequest.getGraduatedUniversity(), createdDentist.getGraduatedUniversity());

        assertEquals(id, createdDentist.getId());
        verify(dentistRepository).save(any());
    }

    @Test
    public void create_WhenDentistIsNull_ShouldThrowInvalidRequestException() {
        //Initialize Test
        DentistRequest dentistRequest = null;

        //Run Test
        assertThrows(InvalidRequestException.class, () -> dentistService.create(dentistRequest));

        //Verify Result
        verify(dentistRepository, times(0)).save(any());
    }

    private DentistRequest createDentistRequest() {
        return DentistRequest.builder()
                .email("ugur@mgail.com")
                .firstName("ugur")
                .lastName("soy isim")
                .dateBirth(Date.valueOf("1999-01-23"))
                .dentalSpecialization(DentalSpecialization.ORTHODONTICS)
                .phoneNumber("1234")
                .graduatedUniversity("Yildiz")
                .build();
    }
}