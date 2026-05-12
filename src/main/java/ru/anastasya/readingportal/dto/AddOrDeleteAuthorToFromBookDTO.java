package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;

public record AddOrDeleteAuthorToFromBookDTO(
        @NotNull
        Long bookId,
        @NotNull
        Long authorId,
        @NotNull
        Long currentUserId,
        @NotNull
        Integer version) {
}
