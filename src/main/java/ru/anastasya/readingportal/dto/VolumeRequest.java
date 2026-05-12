package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record VolumeRequest(
        @NotBlank
        @Size(min = 2, max = 250)
        String title,
        @PositiveOrZero
        @NotNull
        double volumeNumber) {
}
