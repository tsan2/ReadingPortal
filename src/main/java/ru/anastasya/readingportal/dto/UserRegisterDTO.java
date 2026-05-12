package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank
        @Size(min = 2, max = 30)
        String nickname,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 4)
        String password) {
}
