package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Смена пароля по старому паролю")
public record ChangePasswordByOldPasswordDTO(
        @Schema(description = "старый пароль", example = "1234")
        @NotEmpty
        String oldPassword,
        @Schema(description = "новый пароль", example = "12345678")
        @NotEmpty
        @Size(min = 4)
        String newPassword,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        @NotNull
        Integer version) {
}
