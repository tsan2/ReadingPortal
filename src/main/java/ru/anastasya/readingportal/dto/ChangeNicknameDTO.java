package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidUserNickname;

@Schema(description = "Смена никнейма")
public record ChangeNicknameDTO(
        @Schema(description = "новый никнейм", example = "tsan20")
        @ValidUserNickname
        String newNickname,
        @Schema(description = "версия записи пользователя из базы данных", example = "1")
        @NotNull
        Integer version) {
}
