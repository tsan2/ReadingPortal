package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeChapterNumberDTO(
        @Schema(description = "первая часть номера главы", example = "1")
        @PositiveOrZero(message = "Номер главы не может быть меньше 0")
        @NotNull
        int chapterMainNumber,
        @Schema(description = "вторая часть номера главы (подглава)", example = "1")
        @PositiveOrZero(message = "Номер главы не может быть меньше 0")
        @NotNull
        int chapterSubNumber,
        @NotNull
        Integer version
) {
}
