package ru.anastasya.readingportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ErrorResponse(
        HttpStatus status,
        String message,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime timestamp){
}
