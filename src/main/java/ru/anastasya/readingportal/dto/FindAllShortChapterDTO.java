package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Поиск всех глав, прикрепленных к книге или к тому")
public record FindAllShortChapterDTO(
        @Schema(description = "айди книги (необязательное поле)")
        Long bookId,
        @Schema(description = "айди тома (необязательное поле)")
        Long volumeId
) {
}
