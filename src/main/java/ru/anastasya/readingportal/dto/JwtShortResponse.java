package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о выданных JWT токенах")
public record JwtShortResponse(
        @Schema(description = "access токен")
        String accessToken) {
}
