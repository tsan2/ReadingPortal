package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;

public record ChangeEmailDTO(
        @NotNull
        Long id,
        @NotBlank
        String password,
        @ValidUserEmail
        String newEmail,
        @NotNull
        int version) {
}
