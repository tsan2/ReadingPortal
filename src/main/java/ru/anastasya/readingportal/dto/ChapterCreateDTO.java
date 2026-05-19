package ru.anastasya.readingportal.dto;

import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record ChapterCreateDTO(
        Long bookId,
        @ValidTitle
        String title,
        int chapterMainNumber,
        int chapterSubNumber,
        Long volumeId) {
}

