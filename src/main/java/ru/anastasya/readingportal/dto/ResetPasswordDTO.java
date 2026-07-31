package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import ru.anastasya.readingportal.dto.validation.ValidPassword;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;

@Schema(description = "Восстановление пароля")
public record ResetPasswordDTO(
        @Schema(description = "емейл", example = "tsan@gmail.com")
        @ValidUserEmail
        String email,
        @Schema(description = "код для восстановления", example = "123456")
        @NotBlank
        String code,
        @Schema(description = "новый пароль", example = "new_password")
        @ValidPassword
        String newPassword) {
}
