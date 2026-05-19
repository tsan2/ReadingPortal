package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidGenreName;

public record GenreRequestDTO(
        @ValidGenreName
        String name) {
}
