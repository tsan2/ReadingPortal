package ru.anastasya.readingportal.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.anastasya.readingportal.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {


    @Query("""
            SELECT DISTINCT b FROM Book b
            LEFT JOIN b.authors
            LEFT JOIN b.genres g WHERE
            (:authorsIds IS NULL OR a.id IN :authorsIds) AND
            (:genresIds IS NULL g.id IN :genresIds)""")
    Page<Book> findBooksByBookFilter(@Param("authorsIds") List<Long> authorsIds,
                                     @Param("genresIds") List<Long> genresIds,
                                     Pageable pageable);

    void deleteAllByUserId(Long userId);
}
