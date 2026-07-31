package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Профиль пользователя")
public record ProfileDTO(
        @Schema(description = "айди пользователя", example = "1")
        Long id,
        @Schema(description = "никнейм", example = "tsan")
        String nickname,
        @Schema(description = "емейл", example = "tsan@gmail.com")
        String email,
        @Schema(description = "время создания аккаунта", example = "2026-02-16T23:27:10.932053")
        LocalDateTime createdAt,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        Integer version) {
}
