package ru.anastasya.readingportal.dto;

public record AddOrDeleteGenreToFromBookDTO(Long bookId, Long genreId, Long currentUserId, Integer version) {
}
