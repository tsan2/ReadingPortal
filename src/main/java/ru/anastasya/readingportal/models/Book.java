package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class Book {

    @Id
    @SequenceGenerator(name = "book_seq", sequenceName = "book_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    private Long id;
    @Column(nullable = false)
    private String title;
    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "date_changed")
    private LocalDateTime dateChanged;
    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Version
    private Integer version;

    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Genre> genres = new HashSet<>();

    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> authors = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Volume> volumes = new ArrayList<>();

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

