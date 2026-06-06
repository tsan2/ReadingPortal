package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;

@Schema(description = "Смена емейла")
public record ChangeEmailDTO(
        @Schema(description = "пароль", example = "1234")
        @NotBlank
        String password,
        @Schema(description = "новый емейл", example = "tsan20@gmail.com")
        @ValidUserEmail
        String newEmail,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        @NotNull
        int version) {
}
