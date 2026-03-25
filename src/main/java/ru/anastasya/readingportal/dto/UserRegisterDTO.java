package ru.anastasya.readingportal.dto;

import java.io.Serializable;

public record UserRegisterDTO(String nickname, String email, String password) {
}
