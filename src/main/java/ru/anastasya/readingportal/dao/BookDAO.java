package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BookDAO {

    private static final String FIND_BOOK_BY_ID_SQL = "SELECT * FROM books WHERE id = ?;";
    private static final String FIND_BOOKS_BY_GENRE_ID_SQL = """
            SELECT b.id, b.title, b.date_changed, b.created_at
            FROM books b
            JOIN books_genres bg
            ON bg.book_id=b.id
            WHERE genre_id = ?;
            """;
    private static final String FIND_BOOKS_BY_AUTHOR_ID_SQL = """
            SELECT b.id, b.title, b.date_changed, b.created_at
            FROM books b
            JOIN books_authors ba
            ON ba.book_id=b.id
            WHERE ba.user_id = ?;
            """;
    private static final String UPDATE_BOOK_SQL = """
            UPDATE books
            SET title = ?
            WHERE id = ?;
            """;
    private static final String SAVE_BOOK_SQL = "INSERT INTO books(title) VALUES (?);";
    private static final String ADD_AUTHOR_TO_BOOK_SQL = "INSERT INTO books_authors(user_id, book_id) VALUES (?, ?);";
    private static final String DELETE_AUTHOR_FROM_BOOK_SQL = "DELETE FROM books_authors WHERE user_id = ? and book_id = ?;";
    private static final String DELETE_ALL_AUTHORS_FROM_BOOK_SQL = "DELETE FROM books_authors WHERE book_id = ?;";
    private static final String ADD_GENRE_TO_BOOK_SQL = "INSERT INTO books_genres(book_id, genre_id) VALUES (?, ?);";
    private static final String DELETE_GENRE_FROM_BOOK_SQL = "DELETE FROM books_genres WHERE book_id = ? and genre_id = ?;";
    private static final String DELETE_ALL_GENRES_FROM_BOOK_SQL = "DELETE FROM books_genres WHERE book_id = ?;";
    private static final String DELETE_BOOK_SQL = "DELETE FROM books WHERE id = ?;";

    //поч невозможно достать книгу с авторами и жанрами

    public void save(Book book){
        Objects.requireNonNull(book, "Нельзя сохранить null book");
        CRUDutil.insert(SAVE_BOOK_SQL, book.getTitle());
    }

    public void update(Book book){
        Objects.requireNonNull(book, "Нельзя изменить null book");
        CRUDutil.update(UPDATE_BOOK_SQL, book.getTitle(), book.getId());
    }

    public void delete(Long bookId){
        CRUDutil.update(DELETE_BOOK_SQL, bookId);
    }

    public void addAuthorToBook(Long bookId, Long authorId){
        CRUDutil.insert(ADD_AUTHOR_TO_BOOK_SQL, authorId, bookId);
    }

    public void deleteAuthorFromBook(Long bookId, Long authorId){
        CRUDutil.update(DELETE_AUTHOR_FROM_BOOK_SQL, authorId, bookId);
    }

    public void deleteAllAuthorsFromBook(Long bookId){
        CRUDutil.update(DELETE_ALL_AUTHORS_FROM_BOOK_SQL, bookId);
    }

    public void addGenreToBook(Long bookId, Long genreId){
        CRUDutil.insert(ADD_GENRE_TO_BOOK_SQL, bookId, genreId);
    }

    public void deleteGenreFromBook(Long bookId, Long genreId){
        CRUDutil.update(DELETE_GENRE_FROM_BOOK_SQL, bookId, genreId);
    }

    public void deleteAllGenresFromBook(Long bookId){
        CRUDutil.update(DELETE_ALL_GENRES_FROM_BOOK_SQL, bookId);
    }

    public Book findById(Long id){
        return CRUDutil.readOne(FIND_BOOK_BY_ID_SQL, this::Map, id);
    }

    public List<Book> findBooksByGenreId(Long genreId){
        return CRUDutil.readMany(FIND_BOOKS_BY_GENRE_ID_SQL, this::Map, genreId);
    }

    public List<Book> findBooksByAuthorId(Long authorId){
        return CRUDutil.readMany(FIND_BOOKS_BY_AUTHOR_ID_SQL, this::Map, authorId);
    }

    private Book Map(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        LocalDateTime date_changed = resultSet.getTimestamp("date_changed").toLocalDateTime();
        LocalDateTime created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Book(id, title, date_changed, created_at);
    }

}
