package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidPassword;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;

public record ResetPasswordDTO(
        @ValidUserEmail
        String email,
        @NotBlank
        String code,
        @ValidPassword
        String newPassword) {
}
