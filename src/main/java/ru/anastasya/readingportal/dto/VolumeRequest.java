package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record VolumeRequest(
        @ValidTitle
        String title,
        @PositiveOrZero
        @NotNull
        double volumeNumber) {
}
