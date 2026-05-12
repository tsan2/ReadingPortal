package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeEmailDTO(
        @NotNull
        Long id,
        @NotBlank
        String password,
        @NotBlank
        @Email
        String newEmail) {
}
