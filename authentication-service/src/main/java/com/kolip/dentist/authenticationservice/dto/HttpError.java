package com.kolip.dentist.authenticationservice.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Data
@Builder
public class HttpError {
    private String errorMessage;
    private HttpStatusCode httpStatusCode;
    private List<String> errors;
}
