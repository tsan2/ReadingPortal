package ru.anastasya.readingportal.dao;

import ru.anastasya.readingportal.models.RememberMeToken;
import ru.anastasya.readingportal.utils.CRUDutil;

public class RememberMeTokenDAO {

    private static final RememberMeTokenDAO rememberMeTokenDAO = new RememberMeTokenDAO();
    private RememberMeTokenDAO(){

    }

    public static RememberMeTokenDAO getInstance(){
        return rememberMeTokenDAO;
    }

    private final String SAVE_TOKEN_SQL = """
            INSERT INTO remember_me_tokens(user_id, token_hash, expires_at)
            VALUES (?, ?, ?);""";
    private final String DELETE_ALL_TOKEN_BY_USER_ID_SQL = "DELETE FROM remember_me_tokens WHERE user_id = ?;";


    public void save(RememberMeToken rememberMeToken){
        CRUDutil.insert(SAVE_TOKEN_SQL, rememberMeToken.getUserId(), rememberMeToken.getToken_hash(), rememberMeToken.getExpiresAt());
    }

    public void deleteAllTokenByUserId(Long userId){
        CRUDutil.update(DELETE_ALL_TOKEN_BY_USER_ID_SQL, userId);
    }
}
