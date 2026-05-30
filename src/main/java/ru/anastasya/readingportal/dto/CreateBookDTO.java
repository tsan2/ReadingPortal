package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Создание книги")
public record CreateBookDTO(
        @Schema(description = "название книги", example = "Гарри Поттер")
        @ValidTitle
        String title) {
}
