package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Обновление токенов")
public record RefreshDTO(
        @Schema(description = "refresh токен")
        String refreshToken) {
}
