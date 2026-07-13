package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.anastasya.readingportal.dto.validation.ValidPassword;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;
import ru.anastasya.readingportal.dto.validation.ValidUserNickname;

@Schema(description = "Регистрация пользователя")
public record UserRegisterDTO(
        @Schema(description = "никнейм пользователя", example = "tsan", requiredMode = Schema.RequiredMode.REQUIRED)
        @ValidUserNickname
        String nickname,
        @Schema(description = "емейл пользователя", example = "tsan@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @ValidUserEmail
        String email,
        @Schema(description = "пароль", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
        @ValidPassword
        String password) {
}
