package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeVolumeNumberDTO(
        @NotNull
        Long id,
        @NotNull
        @PositiveOrZero
        double volumeNumber,
        @NotNull
        Long currentUserId,
        @NotNull
        Integer version) {
}
