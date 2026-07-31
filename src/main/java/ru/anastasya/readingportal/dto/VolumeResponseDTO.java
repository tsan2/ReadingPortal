package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Информация о томе")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VolumeResponseDTO{
    @Schema(description = "айди", example = "1")
    private Long id;
    @Schema(description = "название тома", example = "Философский камень")
    private String title;
    @Schema(description = "первая часть номера тома (целая часть, до точки)", example = "1")
    private int volumeMainNumber;
    @Schema(description = "вторая часть номера тома (дробная часть, после точки)", example = "1")
    private int volumeSubNumber;
    @Schema(description = "книга")
    private BookSummaryDTO book;
    @Schema(description = "версия записи пользователя из базы данных", example = "1")
    private int version;
    @Schema(description = "сообщение-предупреждение", example = "Вы пропускаете номер тома. Последний номер сейчас: 3")
    private String warningMessage;

}
