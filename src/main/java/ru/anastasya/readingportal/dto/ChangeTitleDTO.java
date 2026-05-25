package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record ChangeTitleDTO(
        @ValidTitle
        String newTitle,
        @NotNull
        Integer version) {
}
