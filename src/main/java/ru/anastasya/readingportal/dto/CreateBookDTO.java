package ru.anastasya.readingportal.dto;

import ru.anastasya.readingportal.dto.validation.ValidTitle;

public record CreateBookDTO(
        @ValidTitle
        String title) {
}
