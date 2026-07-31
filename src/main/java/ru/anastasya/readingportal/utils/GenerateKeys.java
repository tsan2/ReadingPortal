package ru.anastasya.readingportal.utils;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Base64;

public class GenerateKeys {
    public static void main(String[] args){
        SecretKey accessSecretKey = Jwts.SIG.HS256.key().build();
        SecretKey refreshSecretKey = Jwts.SIG.HS256.key().build();

        String accessKey = Base64.getEncoder().encodeToString(accessSecretKey.getEncoded());
        String refreshKey = Base64.getEncoder().encodeToString(refreshSecretKey.getEncoded());

        System.out.println(accessKey);
        System.out.println(refreshKey);
    }
}
