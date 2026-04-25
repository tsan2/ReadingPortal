package ru.anastasya.readingportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookSortStrategy {

    NEWEST("Сортировка по новизне"),
    ALPHABETICAL("Сортировка по алфавиту");

    private final String description;

}
