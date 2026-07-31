package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anastasya.readingportal.models.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

}
