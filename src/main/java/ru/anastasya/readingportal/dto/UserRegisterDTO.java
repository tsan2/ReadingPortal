package ru.anastasya.readingportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.anastasya.readingportal.dto.validation.ValidPassword;
import ru.anastasya.readingportal.dto.validation.ValidUserEmail;
import ru.anastasya.readingportal.dto.validation.ValidUserNickname;

public record UserRegisterDTO(
        @ValidUserNickname
        String nickname,
        @ValidUserEmail
        String email,
        @ValidPassword
        String password) {
}
