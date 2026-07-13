package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Создание шаблона главы (без текста)")
public record ChapterCreateDTO(
        @ValidTitle
        @Schema(description = "название", example = "восхождение")
        String title,
        @PositiveOrZero
        @Schema(description = "первая часть номера главы (до точки)", example = "1")
        int chapterMainNumber,
        @PositiveOrZero(message = "Номер главы не может быть меньше 0")
        @Schema(description = "вторая часть номера главы (после точки, подглава)", example = "1")
        int chapterSubNumber) {
}

