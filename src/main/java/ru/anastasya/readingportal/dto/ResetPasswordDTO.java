package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String code,
        @NotBlank
        @Size(min = 4)
        String newPassword) {
}
