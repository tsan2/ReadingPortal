package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BookFilterRequestDTO(
        @Parameter(description = "название книги", example = "Гарри Поттер")
        String title,
        @Parameter(description = "список айди авторов")
        List<Long> authorsIds,
        @Parameter(description = "список айди жанров")
        List<Long> genresIds,
        @Parameter(description = "сортировка",
        schema = @Schema(allowableValues = {"newest", "alphabetical"}),
        example = "newest")
        String sortStrategy) {
}
