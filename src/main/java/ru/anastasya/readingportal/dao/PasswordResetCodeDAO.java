package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.PasswordResetCode;
import ru.anastasya.readingportal.utils.CRUDutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PasswordResetCodeDAO {

    private static final String SAVE_CODE_SQL = "INSERT INTO password_reset_codes(user_id, code, expires_at) VALUES (?, ?, ?);";
    private static final String FIND_VALID_CODE_SQL = """
            SELECT * FROM password_reset_codes
            WHERE user_id = ? and code = ? and expires_at > CURRENT_TIMESTAMP;""";
    private static final String DELETE_ALL_CODES_BY_USER_SQL = """
            DELETE FROM password_reset_codes
            WHERE user_id = ?;""";


    public void saveCode(PasswordResetCode resetCode){
        CRUDutil.insert(SAVE_CODE_SQL, resetCode.getUser_id(), resetCode.getCode(), resetCode.getExpires_at());
    }

    public PasswordResetCode findValidCode(Long idUser, String code){
        PasswordResetCode resetCode = CRUDutil.readOne(FIND_VALID_CODE_SQL, this::map, idUser, code);
        return resetCode;
    }

    public void deleteAllCodesByUserId(Long UserId){
        CRUDutil.update(DELETE_ALL_CODES_BY_USER_SQL, UserId);
    }

    private PasswordResetCode map(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        Long userId = resultSet.getLong("user_id");
        String code = resultSet.getString("code");
        LocalDateTime expires_at = resultSet.getTimestamp("expires_at").toLocalDateTime();
        LocalDateTime created_at = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new PasswordResetCode(id, userId, code, expires_at, created_at);
    }

}
