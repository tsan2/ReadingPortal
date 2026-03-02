package ru.anastasya.readingportal.utils;

import ru.anastasya.readingportal.models.User;

public class SessionContext {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void login(User user) {
        SessionContext.user = user;
    }

    public static void logout(){
        SessionContext.user = null;
    }

    public static boolean isLogged(){
        return SessionContext.user!=null;
    }
}
