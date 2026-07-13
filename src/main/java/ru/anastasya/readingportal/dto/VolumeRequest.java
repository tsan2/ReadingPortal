package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Создание тома")
public record VolumeRequest(
        @Schema(description = "название тома", example = "Философский камень")
        @ValidTitle
        String title,
        @Schema(description = "первая часть номера тома", example = "1")
        @PositiveOrZero
        @NotNull
        int volumeMainNumber,
        @Schema(description = "вторая часть номера тома (подтом)", example = "1")
        @PositiveOrZero
        @NotNull
        int volumeSubNumber) {
}
