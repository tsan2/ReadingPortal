package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о книге")
public record BookSummaryDTO(
        @Schema(description = "айди книги", example = "1")
        Long id,
        @Schema(description = "название книги", example = "Гарри Поттер")
        String title) {
}
