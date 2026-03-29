package ru.anastasya.readingportal.dto;

import java.util.List;

public class BookFilter {

    private List<Long> authorsIds;
    private List<Long> genresIds;
    private BookSortStrategy bookSortStrategy;
    private int page;
    private int size;

    public BookFilter(List<Long> authorsIds, List<Long> genresIds, BookSortStrategy bookSortStrategy, int page, int size) {
        this.authorsIds = authorsIds;
        this.genresIds = genresIds;
        this.bookSortStrategy = bookSortStrategy;
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Long> getAuthorsIds() {
        return authorsIds;
    }

    public void setAuthorsIds(List<Long> authorsIds) {
        this.authorsIds = authorsIds;
    }

    public List<Long> getGenresIds() {
        return genresIds;
    }

    public void setGenresIds(List<Long> genresIds) {
        this.genresIds = genresIds;
    }

    public BookSortStrategy getBookSortStrategy() {
        return bookSortStrategy;
    }

    public void setBookSortStrategy(BookSortStrategy bookSortStrategy) {
        this.bookSortStrategy = bookSortStrategy;
    }
}
