package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "genres")
@Entity
public class Genre {

    @Id
    @SequenceGenerator(name = "genre_seq", sequenceName = "genre_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    public Genre(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Genre)) return false;
        Genre genre = (Genre) obj;

        return Objects.equals(genre.getName(), this.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
