package ru.anastasya.readingportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BookFilter {

    private List<Long> authorsIds;
    private List<Long> genresIds;
    private BookSortStrategy bookSortStrategy;


}
