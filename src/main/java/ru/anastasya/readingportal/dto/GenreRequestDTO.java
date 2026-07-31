package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.anastasya.readingportal.dto.validation.ValidGenreName;

@Schema(description = "Создание жанра")
public record GenreRequestDTO(
        @Schema(description = "название", example = "хоррор")
        @ValidGenreName
        String name) {
}
