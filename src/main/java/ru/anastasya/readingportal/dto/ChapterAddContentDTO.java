package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidContent;

@Schema(description = "Добавление текста к главе")
public record ChapterAddContentDTO(
        @Schema(description = "текст главы", example = "очень длинный текст...")
        @ValidContent
        String content,
        @Schema(description = "версия записи из базы данных", example = "1")
        @NotNull
        int version
) {
}
