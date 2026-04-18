package ru.anastasya.readingportal.utils;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int codeLength = 6;

    public static String generateCode(){
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 1; i<codeLength; i++){
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }
}
