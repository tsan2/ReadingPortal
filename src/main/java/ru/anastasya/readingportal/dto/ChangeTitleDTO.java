package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Смена названия")
public record ChangeTitleDTO(
        @Schema(description = "название", example = "Гарри Поттер")
        @ValidTitle
        String newTitle,
        @Schema(description = "версия записи из базы данных", example = "1")
        @NotNull
        Integer version) {
}
