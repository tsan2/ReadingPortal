package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.dto.BookFilter;
import ru.anastasya.readingportal.dto.BookSortStrategy;
import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            SET title = ?,
            date_changed = ?
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
    private static final String EXISTS_BOOK_SQL = "SELECT COUNT(*) FROM books WHERE id = ?;";
    private static final String IS_GENRE_ADDED_SQL = "SELECT COUNT(*) FROM books_genres WHERE book_id = ? and genre_id = ?;";
    private static final String IS_AUTHOR_ADDED_SQL = "SELECT COUNT(*) FROM books_authors WHERE book_id = ? and author_id = ?;";

    //поч невозможно достать книгу с авторами и жанрами

    public Long save(Book book){
        Objects.requireNonNull(book, "Нельзя сохранить null book");
        Long newId = CRUDutil.insert(SAVE_BOOK_SQL, book.getTitle());
        return newId;
    }

    public void update(Book book){
        Objects.requireNonNull(book, "Нельзя изменить null book");
        CRUDutil.update(UPDATE_BOOK_SQL, book.getTitle(), book.getId(), LocalDateTime.now());
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

    public List<Book> findBooksByBookFilter(BookFilter bookFilter) {
        StringBuilder sql = new StringBuilder("""
                SELECT *
                FROM books b
                WHERE 1=1""");
        List<Object> params = new ArrayList<>();
        if (bookFilter.getAuthorId() != null) {
            sql.append(" AND id IN (SELECT book_id FROM books_authors WHERE user_id = ?)");
            params.add(bookFilter.getAuthorId());
        }
        if (bookFilter.getGenreId() != null) {
            sql.append(" AND id IN (SELECT book_id FROM books_genres WHERE genre_id = ?)");
            params.add(bookFilter.getGenreId());
        }
        if (bookFilter.getBookSortStrategy() != null) {
            sql.append(" " + bookFilter.getBookSortStrategy().getSql());
        }

        return CRUDutil.readMany(sql.toString(), this::Map, params);
    }

    public boolean exists(Long id){
        int count =  CRUDutil.readOne(EXISTS_BOOK_SQL, rs -> rs.getInt(1), id);
        return count > 0;
    }

    public boolean isGenreAdded(Long bookId, Long genreId){
        int count =  CRUDutil.readOne(IS_GENRE_ADDED_SQL, rs -> rs.getInt(1), bookId, genreId);
        return count > 0;
    }

    public boolean isAuthorAdded(Long bookId, Long authorId){
        int count =  CRUDutil.readOne(IS_AUTHOR_ADDED_SQL, rs -> rs.getInt(1), bookId, authorId);
        return count > 0;
    }

    private Book Map(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        LocalDateTime date_changed = resultSet.getTimestamp("date_changed").toLocalDateTime();
        LocalDateTime created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Book(id, title, date_changed, created_at);
    }

}
