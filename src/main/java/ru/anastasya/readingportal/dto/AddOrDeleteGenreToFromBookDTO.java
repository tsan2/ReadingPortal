package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;

public record AddOrDeleteGenreToFromBookDTO(
        @NotNull
        Long bookId,
        @NotNull
        Long genreId,
        @NotNull
        Long currentUserId,
        @NotNull
        Integer version) {
}
