package ru.anastasya.readingportal.dto;

public record ResetPasswordDTO(String email, String code, String newPassword) {
}
