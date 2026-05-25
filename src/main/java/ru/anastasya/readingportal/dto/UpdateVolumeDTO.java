package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record UpdateVolumeDTO(
        @ValidTitle
        String newTitle,
        @NotNull
        Integer version,
        @PositiveOrZero
        Double volumeNumber
) {
}
