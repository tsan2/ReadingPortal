package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о главе")
public record ChapterShortDTO(
        @Schema(description = "айди", example = "1")
        Long id,
        @Schema(description = "название", example = "восхождение")
        String title,
        @Schema(description = "первая часть номера главы (целая часть, до точки)", example = "1")
        int chapterMainNumber,
        @Schema(description = "вторая часть номера главы (дробная часть, после точки)", example = "1")
        int chapterSubNumber,
        @Schema(description = "версия в базе данных", example = "1")
        int version) {
}
