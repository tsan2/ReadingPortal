package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UserDAO{

    private static final String FIND_ALL_USERS_SQL = "SELECT id, nickname, email, created_at FROM users;";
    private static final String FIND_USER_BY_ID_SQL = "SELECT id, nickname, email, created_at FROM users WHERE id=?;";
    private static final String FIND_USER_BY_NICKNAME_SQL = "SELECT id, nickname, email, created_at FROM users WHERE nickname=?;";
    private static final String FIND_USER_BY_EMAIL_SQL = "SELECT id, nickname, email, created_at FROM users WHERE email=?;";
    private static final String SAVE_USER_SQL = "INSERT INTO users(nickname, email, password_hash) VALUES (?, ?, ?, ?);";
    private static final String FIND_USER_BY_BOOK_ID_SQL = """
            SELECT u.id, u.nickname, u.email, u.created_at
            FROM users u
            JOIN books_authors b
            ON b.user_id=u.id
            WHERE b.book_id=3;""";
    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET nickname = ?,
            email = ?
            WHERE id = ?;
            """;
    private static final String UPDATE_USER_PASSWORD_HASH_SQL = """
            UPDATE users
            SET password_hash = ?
            WHERE id = ?;
            """;
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String FIND_FULL_USER_BY_EMAIL_OR_NICKNAME_SQL = """
            SELECT id, nickname, email, password_hash, created_at
            FROM users WHERE email=? OR nickname=?;""";
    private static final String FIND_FULL_USER_BY_ID_SQL = """
            SELECT id, nickname, email, password_hash, created_at
            FROM users WHERE id=?;""";

    public List<User> findAll(){
        List<User> list = CRUDutil.readMany(FIND_ALL_USERS_SQL, this::publicMap);
        return list;
    }

    public User findById(Long id){
        return CRUDutil.readOne(FIND_USER_BY_ID_SQL, this::publicMap, id);
    }

    public User findByNickname(String nickname){
        return CRUDutil.readOne(FIND_USER_BY_NICKNAME_SQL, this::publicMap, nickname);
    }

    public User findByEmail(String email){
        return CRUDutil.readOne(FIND_USER_BY_EMAIL_SQL, this::publicMap, email);
    }

    public List<User> findByBookId(Long id){
        List<User> users = CRUDutil.readMany(FIND_USER_BY_BOOK_ID_SQL, this::publicMap, id);
        return users;
    }

    public void save(User user){
        Objects.requireNonNull(user, "Нельзя сохранить null user");
        CRUDutil.insert(SAVE_USER_SQL, user.getNickname(), user.getEmail(), user.getPassword_hash());
    }

    public void update(User user){
        Objects.requireNonNull(user, "Нельзя изменить null user");
        CRUDutil.update(UPDATE_USER_SQL, user.getNickname(), user.getEmail(), user.getId());
    }

    public void updatePasswordHash(Long id, String passwordHash){
        CRUDutil.update(UPDATE_USER_PASSWORD_HASH_SQL, passwordHash, id);
    }

    public void delete(Long id){
        CRUDutil.update(DELETE_USER_SQL, id);
    }

    public User findFullUserByEmailOrNickname(String emailOrNickname){
        User user = CRUDutil.readOne(FIND_FULL_USER_BY_EMAIL_OR_NICKNAME_SQL, this::fullMap, emailOrNickname, emailOrNickname);
        return user;
    }


    public User findFullUserById(Long id){
        User user = CRUDutil.readOne(FIND_FULL_USER_BY_ID_SQL, this::fullMap, id);
        return user;
    }

    public boolean existsEmail(String email){
        String sql = "SELECT COUNT(id) FROM users WHERE email=?";
        List<Long> list = CRUDutil.readMany(sql, rs -> rs.getLong(1), email);
        return list.isEmpty();
    }

    public boolean existsNickname(String nickname){
        String sql = "SELECT COUNT(id) FROM users WHERE nickname=?";
        List<String> list = CRUDutil.readMany(sql, rs -> rs.getString(1), nickname);
        return list.isEmpty();
    }

    private User publicMap(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String nickname = resultSet.getString("nickname");
        String email = resultSet.getString("email");
        LocalDateTime created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new User(id, nickname, email, created_at);
    }

    private User fullMap(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String nickname = resultSet.getString("nickname");
        String email = resultSet.getString("email");
        String password_hash = resultSet.getString("password_hash");
        LocalDateTime created_at = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new User(id, nickname, email, password_hash, created_at);
    }
}
