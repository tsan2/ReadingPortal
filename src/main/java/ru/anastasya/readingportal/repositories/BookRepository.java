package ru.anastasya.readingportal.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    //потом добавить удаление жанра по айди книги
    //добавить добавление автора к книге
    //удаление автора из книги
    //удаление всех авторов из книги
    //добавление жанра к книге
    //удаление жанра из книги
    //удаление всех жанров из книги
    //удаление всех книг по айди юзера

    //видимо оставить на потом когда будут многие ко многим
//    @Query("""
//            SELECT b FROM Book b WHERE
//            ( b.authorsIds = :authorsIds) AND b.genresIds = :genresIds""")
//    Page<Book> findBooksByBookFilter(@Param("authorsIds") List<Long> authorsIds,
//                                     @Param("genresIds") List<Long> genresIds,
//                                     Pageable pageable);

    //метод проверка есть ли жанр у книги
    //метод проверка есть ли автор у книги
}
