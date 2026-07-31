package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о томе")
public record VolumeSummaryDTO(
        @Schema(description = "айди", example = "1")
        Long id,
        @Schema(description = "название тома", example = "Философский камень")
        String title,
        @Schema(description = "первая часть номера тома (целая часть, до точки)", example = "1")
        int volumeMainNumber,
        @Schema(description = "вторая часть номера тома (дробная часть, после точки)", example = "1")
        int volumeSubNumber) {
}
