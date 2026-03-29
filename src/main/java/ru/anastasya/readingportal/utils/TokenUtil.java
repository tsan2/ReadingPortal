package ru.anastasya.readingportal.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtil {

    private static final String PEPER = "Tegrepro1134mfveps";

    public static String hashToken(String token){
        String hashToken = DigestUtils.sha256Hex(token + PEPER);
        return hashToken;
    }

}
