package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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

    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Genre> genres;

    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> authors;

    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Volume> volumes;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Book)) return false;
        Book book = (Book) obj;
        if (book.getId() == null) return false;
        return Objects.equals(book.getId(), this.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    //использовать только в случае если список книг у автора потом понадобится,
    //загружает всю коллекцию книг автора, создает нагрузку
    public void addAuthor(User user){
        authors.add(user);
        user.getBooks().add(this);
    }
    //тоже самое
    public void removeAuthor(User user){
        authors.remove(user);
        user.getBooks().remove(this);
    }

    public void addVolume(Volume volume){
        volumes.add(volume);
        volume.setBook(this);
    }

    public void removeVolume(Volume volume){
        volumes.remove(volume);
        volume.setBook(null);
    }

    public Book(String title) {
        this.id = null;
        this.title = title;
    }

}

