package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidPassword;
import ru.anastasya.readingportal.utils.ValidationConstants;

@Schema(description = "Авторизация пользователя")
public record UserLoginDTO(
        @NotBlank(message = "Никнейм или емейл не могут быть пустыми")
        @Size(min = 2, message = "Длина никнейма или емейла не соответствует требованиям")
        @Schema(description = "почта или никнейм аккаунта", example = "tsan")
        String emailOrNickname,
        @NotBlank
        @ValidPassword
        @Schema(description = "пароль от аккаунта", example = "my_password")
        String password) {
}
