package ru.anastasya.readingportal.models;

import java.time.LocalDateTime;
import java.util.List;

public class Book {

    private Long id;
    private String title;
    private LocalDateTime date_changed;
    private LocalDateTime created_at;

    private List<Genre> genres;
    private List<User> authors;

    public Book() {
    }

    public Book(String title, LocalDateTime date_changed, LocalDateTime created_at) {
        this.id = null;
        this.title = title;
        this.date_changed = date_changed;
        this.created_at = created_at;
    }

    public Book(String title, LocalDateTime date_changed, LocalDateTime created_at, List<Genre> genres, List<User> authors) {
        this.id = null;
        this.title = title;
        this.date_changed = date_changed;
        this.created_at = created_at;
        this.genres = genres;
        this.authors = authors;
    }

    public Book(Long id, String title, LocalDateTime date_changed, LocalDateTime created_at) {
        this.id = id;
        this.title = title;
        this.date_changed = date_changed;
        this.created_at = created_at;
    }

    public Book(Long id, String title, LocalDateTime date_changed, LocalDateTime created_at, List<Genre> genres, List<User> authors) {
        this.id = id;
        this.title = title;
        this.date_changed = date_changed;
        this.created_at = created_at;
        this.genres = genres;
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date_changed=" + date_changed +
                ", created_at=" + created_at +
                ", genres=" + genres +
                ", authors=" + authors +
                '}';
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
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

    public LocalDateTime getDate_changed() {
        return date_changed;
    }

    public void setDate_changed(LocalDateTime date_changed) {
        this.date_changed = date_changed;
    }
}
