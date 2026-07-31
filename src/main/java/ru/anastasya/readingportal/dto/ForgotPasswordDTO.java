package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Забыл пароль")
public record ForgotPasswordDTO(
        @Schema(description = "адрес электронной почты", example = "tsan@gmail.com")
        @NotBlank
        @Email
        String email) {
}
