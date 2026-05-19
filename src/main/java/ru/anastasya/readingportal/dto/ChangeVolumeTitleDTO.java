package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record ChangeVolumeTitleDTO(
        @NotNull
        Long id,
        @ValidTitle
        String newTitle,
        @NotNull
        Long currentUserId,
        @NotNull
        Integer version) {
}
