package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.Genre;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void save(Genre genre){
        Objects.requireNonNull(genre, "Нельзя сохранить null genre");
        CRUDutil.insert(SAVE_GENRE_SQL, genre.getName());
    }

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

    public List<Genre> findByBookId(Long bookId){
        return CRUDutil.readMany(FIND_GENRES_BY_BOOK_ID_SQL, this::Map, bookId);

    }

    private Genre Map(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }
}
