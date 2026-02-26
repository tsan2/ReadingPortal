package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Book;
import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GenreDAO {

    private static final String SAVE_GENRE_SQL = "INSERT INTO genres(name) VALUES (?)";
    private static final String UPDATE_GENRE_SQL = """
            UPDATE genres
            SET name = ?
            WHERE id = ?;
            """;
    private static final String FIND_GENRES_BY_BOOK_ID_SQL = """
            SELECT g.id, g.name
            FROM genres g
            JOIN books_genres bg
            ON g.id=bg.genre_id
            WHERE bg.book_id=1;""";
    private static final String DELETE_GENRE_SQL = "DELETE FROM genres WHERE id = ?;";
    private static final String FIND_GENRE_BY_ID_SQL = "SELECT * FROM genres WHERE id = ?;";
    private static final String FIND_GENRE_BY_NAME_SQL = "SELECT * FROM genres WHERE name = ?;";
    private static final String FIND_ALL_GENRES_SQL = "SELECT * FROM genres;";
    private static final String EXISTS_GENRE_SQL = "SELECT COUNT(*) FROM genres WHERE id = ?;";

    public void save(Genre genre){
        Objects.requireNonNull(genre, "Нельзя сохранить null genre");
        CRUDutil.insert(SAVE_GENRE_SQL, genre.getName());
    }

    //это вообще зачем
    public void update(Genre genre){
        Objects.requireNonNull(genre, "Нельзя изменить null genre");
        CRUDutil.update(UPDATE_GENRE_SQL, genre.getName(), genre.getId());
    }

    public void delete(Long id){
        CRUDutil.update(DELETE_GENRE_SQL, id);
    }

    public Genre findById(Long id){
        return CRUDutil.readOne(FIND_GENRE_BY_ID_SQL, this::Map, id);
    }

    public Genre findByName(String name){
        return CRUDutil.readOne(FIND_GENRE_BY_NAME_SQL, this::Map, name);
    }

    public List<Genre> findAllByBookId(Long bookId){
        return CRUDutil.readMany(FIND_GENRES_BY_BOOK_ID_SQL, this::Map, bookId);

    }

    public List<Genre> findAll(){
        return CRUDutil.readMany(FIND_ALL_GENRES_SQL, this::Map);
    }

    public HashMap<Long, List<Genre>> findAllGenresOfBooks(List<Book> books){
        StringBuilder sql = new StringBuilder("""
                SELECT ba.book_id, g.id, g.name FROM genres g
                JOIN books_genres bg
                ON bg.genre_id=g.id
                WHERE bg.book_id IN (""");
        for (int i = 0; i<books.size()-1; i++){
            sql.append(books.get(i));
            sql.append(", ");
        }
        sql.append(books.getLast());
        sql.append(")");

        return CRUDutil.readHashMapKeyAndObjects(sql.toString(), "book_id", Long.class, this::Map);
    }

    public boolean exists(Long id){
        int count =  CRUDutil.readOne(EXISTS_GENRE_SQL, rs -> rs.getInt(1), id);
        return count > 0;
    }

    private Genre Map(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }
}
