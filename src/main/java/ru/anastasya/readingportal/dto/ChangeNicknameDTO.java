package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeNicknameDTO(
        @NotNull
        Long id,
        @NotBlank
        @Size(min = 2, max = 30)
        String newNickname,
        @NotNull
        Integer version) {
}
