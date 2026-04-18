package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "genres")
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

}
