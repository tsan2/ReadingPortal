package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о жанре")
public record GenreResponseDTO(
        @Schema(description = "айди", example = "1")
        Long id,
        @Schema(description = "название", example = "хоррор")
        String name) {
}
