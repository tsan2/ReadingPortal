package ru.anastasya.readingportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anastasya.readingportal.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);
    //добавить поиск по id книги
    //добавить batch поиск по листу книг(возвращает map с ключем id книги и значением списком жанров

}
