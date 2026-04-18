package ru.anastasya.readingportal.dto;

public record UserLoginDTO(String emailOrNickname, String password, boolean rememberMe) {
}
