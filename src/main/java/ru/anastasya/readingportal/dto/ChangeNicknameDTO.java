package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidUserNickname;

public record ChangeNicknameDTO(
        @NotNull
        Long id,
        @ValidUserNickname
        String newNickname,
        @NotNull
        Integer version) {
}
