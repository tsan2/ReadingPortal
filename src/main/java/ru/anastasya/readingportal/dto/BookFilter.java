package ru.anastasya.readingportal.dto;

public class BookFilter {

    private Long authorId;
    private Long genreId;
    private BookSortStrategy bookSortStrategy;

    public BookFilter() {
        this.authorId = null;
        this.genreId = null;
        this.bookSortStrategy = null;
    }

    public BookFilter(Long authorId, Long genreId, BookSortStrategy bookSortStrategy) {
        this.authorId = authorId;
        this.genreId = genreId;
        this.bookSortStrategy = bookSortStrategy;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public BookSortStrategy getBookSortStrategy() {
        return bookSortStrategy;
    }

    public void setBookSortStrategy(BookSortStrategy bookSortStrategy) {
        this.bookSortStrategy = bookSortStrategy;
    }
}
