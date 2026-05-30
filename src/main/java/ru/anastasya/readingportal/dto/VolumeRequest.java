package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Создание тома")
public record VolumeRequest(
        @Schema(description = "название тома", example = "Философский камень")
        @ValidTitle
        String title,
        @Schema(description = "номер тома", example = "1.1")
        @PositiveOrZero
        @NotNull
        double volumeNumber) {
}
