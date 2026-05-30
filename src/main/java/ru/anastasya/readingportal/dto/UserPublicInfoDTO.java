package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Публичная информация о пользователе")
public record UserPublicInfoDTO(
        @Schema(description = "айди пользователя", example = "1")
        Long id,
        @Schema(description = "никнейм пользователя", example = "tsan")
        String nickname,
        @Schema(description = "время создания аккаунта", example = "2026-02-16T23:27:10.932053")
        LocalDateTime createdAt) {
}
