package ru.anastasya.readingportal.dto;

public record ChangeBookTitleDTO(Long bookId, String newTitle, Long currentUserId, Integer version) {
}
