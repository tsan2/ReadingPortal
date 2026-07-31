package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeVolumeNumberDTO(
        @Schema(description = "первая часть номера тома", example = "1")
        @PositiveOrZero
        @NotNull
        int volumeMainNumber,
        @Schema(description = "вторая часть номера тома (подтом)", example = "1")
        @PositiveOrZero
        @NotNull
        int volumeSubNumber,
        @NotNull
        Integer version
) {
}
