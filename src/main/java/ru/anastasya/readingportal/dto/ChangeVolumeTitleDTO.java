package ru.anastasya.readingportal.dto;

public record ChangeVolumeTitleDTO(Long id, String newTitle, Long currentUserId, Integer version) {
}
