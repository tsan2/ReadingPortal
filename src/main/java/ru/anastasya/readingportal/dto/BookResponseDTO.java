package ru.anastasya.readingportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.anastasya.readingportal.models.Genre;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookResponseDTO {

    private Long id;
    private String title;
    private LocalDateTime dateChanged;
    private LocalDateTime createdAt;
    private List<AuthorShortDTO> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

    public BookResponseDTO(Long id, String title, LocalDateTime dateChanged,
                           LocalDateTime createdAt, List<AuthorShortDTO> authors, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.dateChanged = dateChanged;
        this.createdAt = createdAt;
        this.authors = authors;
        this.genres = genres;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<AuthorShortDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorShortDTO> authors) {
        this.authors = authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
