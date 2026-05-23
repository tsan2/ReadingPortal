package ru.anastasya.readingportal.dto;

import java.util.List;

public record BookFilterRequestDTO(
        String title,
        List<Long> authorsIds,
        List<Long> genresIds,
        String sortStrategy) {
}
