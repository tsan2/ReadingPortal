package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.RememberMeTokenDAO;
import ru.anastasya.readingportal.models.RememberMeToken;

public class RememberMeTokenService {

    private final static RememberMeTokenService rememberMeTokenService = new RememberMeTokenService();
    private final static RememberMeTokenDAO rememberMeTokenDAO = RememberMeTokenDAO.getInstance();

    private RememberMeTokenService(){

    }

    public static RememberMeTokenService getInstance(){
        return rememberMeTokenService;
    }

    public void createRememberMeToken(RememberMeToken rememberMeToken){
        rememberMeTokenDAO.save(rememberMeToken);
    }

    public void deleteAllRememberMeTokenByUserId(Long userId){
        rememberMeTokenDAO.deleteAllTokenByUserId(userId);
    }

}
