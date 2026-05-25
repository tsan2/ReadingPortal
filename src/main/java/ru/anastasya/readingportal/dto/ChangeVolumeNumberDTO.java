package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeVolumeNumberDTO(
        @NotNull
        @PositiveOrZero
        double volumeNumber,
        @NotNull
        Integer version) {
}
