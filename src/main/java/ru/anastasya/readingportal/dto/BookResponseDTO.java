package ru.anastasya.readingportal.dto;

//import ru.anastasya.readingportal.models.Genre;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Schema(description = "Информация о книге")
public record BookResponseDTO(
        @Schema(description = "айди книги", example = "1")
        Long id,
        @Schema(description = "название книги", example = "Гарри Поттер")
        String title,
        @Schema(description = "время последнего изменения книги", example = "2026-02-16T23:27:10.932053")
        LocalDateTime dateChanged,
        @Schema(description = "время создания книги", example = "2026-02-16T23:27:10.932053")
        LocalDateTime createdAt,
        @Schema(description = "версия записи книги из базы данных", example = "1")
        Integer version,
        @Schema(description = "жанры")
        Set<GenreResponseDTO> genres,
        @Schema(description = "авторы")
        Set<UserSummaryDTO> authors) {
}
