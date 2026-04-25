package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @SequenceGenerator(name = "book_seq", sequenceName = "book_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    private Long id;
    @Column(nullable = false)
    private String title;
    @UpdateTimestamp
    @Column(name = "date_changed")
    private LocalDateTime dateChanged;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Book(String title) {
        this.id = null;
        this.title = title;
    }

}

