package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Изменить главу")
public record ChapterUpdateDTO(
        @Schema(description = "новое название главы (необязательное поле)", example = "Восхождение")
        @ValidTitle
        String newTitle,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        @NotNull
        Integer version,
        @Schema(description = "новая первая часть номера главы (необязательное поле)", example = "1")
        @PositiveOrZero
        Integer newChapterMainNumber,
        @Schema(description = "новая вторая часть номера главы (подглава, необязательное поле)", example = "1")
        @PositiveOrZero
        Integer newChapterSubNumber
) {

}
