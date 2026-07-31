package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Полная информация о главе")
public class ChapterFullDTO{
        @Schema(description = "айди", example = "1")
        private Long id;
        @Schema(description = "название", example = "восхождение")
        private String title;
        @Schema(description = "текст главы", example = "длинный текст...")
        private String content;
        @Schema(description = "первая часть номера главы (целая часть, до точки)", example = "1")
        private int chapterMainNumber;
        @Schema(description = "вторая часть номера главы (дробная часть, после точки)", example = "1")
        private int chapterSubNumber;
        @Schema(description = "версия в базе данных", example = "1")
        private int version;
        @Schema(description = "айди тома", example = "1")
        private Long volumeId;
}
