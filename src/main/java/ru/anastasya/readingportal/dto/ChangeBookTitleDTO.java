package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeBookTitleDTO(
        @NotNull
        Long bookId,
        @NotBlank
        @Size(min = 2, max = 250)
        String newTitle,
        @NotNull
        Long currentUserId,
        @NotNull
        Integer version) {
}
