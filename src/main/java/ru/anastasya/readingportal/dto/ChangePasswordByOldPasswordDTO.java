package ru.anastasya.readingportal.dto;

public record ChangePasswordByOldPasswordDTO(Long id, String oldPassword, String newPassword) {
}
