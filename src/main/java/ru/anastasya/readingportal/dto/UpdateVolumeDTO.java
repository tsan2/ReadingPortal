package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.anastasya.readingportal.dto.validation.ValidTitle;

@Schema(description = "Изменить том")
public record UpdateVolumeDTO(
        @Schema(description = "новое название тома (необязательное поле)", example = "Философский камень")
        @ValidTitle
        String newTitle,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        @NotNull
        Integer version,
        @Schema(description = "новая первая часть номера тома (необязательное поле)", example = "1")
        Integer volumeMainNumber,
        @Schema(description = "новая вторая часть номера тома (подтом, необязательное поле)", example = "1")
        Integer volumeSubNumber
) {
}
