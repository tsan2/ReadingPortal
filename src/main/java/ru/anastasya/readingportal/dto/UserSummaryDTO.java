package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о пользователе")
public record UserSummaryDTO(
        @Schema(description = "айди пользователя", example = "1")
        Long id,
        @Schema(description = "никнейм пользователя", example = "tsan")
        String nickname) {
}
