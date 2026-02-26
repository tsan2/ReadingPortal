package ru.anastasya.readingportal.models;

import java.time.LocalDateTime;
import java.util.List;

public class Book {

    private Long id;
    private String title;
    private LocalDateTime dateChanged;
    private LocalDateTime createdAt;

    private List<Genre> genres;
    private List<User> authors;

    public Book() {
    }

    public Book(String title, LocalDateTime dateChanged, LocalDateTime createdAt) {
        this.id = null;
        this.title = title;
        this.dateChanged = dateChanged;
        this.createdAt = createdAt;
    }

    public Book(String title, LocalDateTime dateChanged, LocalDateTime createdAt, List<Genre> genres, List<User> authors) {
        this.id = null;
        this.title = title;
        this.dateChanged = dateChanged;
        this.createdAt = createdAt;
        this.genres = genres;
        this.authors = authors;
    }

    public Book(Long id, String title, LocalDateTime dateChanged, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.dateChanged = dateChanged;
        this.createdAt = createdAt;
    }

    public Book(Long id, String title, LocalDateTime dateChanged, LocalDateTime createdAt, List<Genre> genres, List<User> authors) {
        this.id = id;
        this.title = title;
        this.dateChanged = dateChanged;
        this.createdAt = createdAt;
        this.genres = genres;
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date_changed=" + dateChanged +
                ", created_at=" + createdAt +
                ", genres=" + genres +
                ", authors=" + authors +
                '}';
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }
}
