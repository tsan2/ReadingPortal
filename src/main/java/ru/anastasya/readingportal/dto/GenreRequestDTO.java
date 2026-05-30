package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidGenreName;

@Schema(description = "Создание жанра")
public record GenreRequestDTO(
        @Schema(description = "название", example = "хоррор")
        @ValidGenreName
        String name) {
}
