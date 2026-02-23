package ru.anastasya.readingportal.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password){
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        return hash;
    }

    public static boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

}
