package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordByOldPasswordDTO(
        @NotNull
        Long id,
        @NotEmpty
        String oldPassword,
        @NotEmpty
        @Size(min = 4)
        String newPassword,
        @NotNull
        Integer version) {
}
