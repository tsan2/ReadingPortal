package ru.anastasya.readingportal.dto;

public record AddOrDeleteAuthorToFromBookDTO(Long bookId, Long authorId, Long currentUserId, Integer version) {
}
