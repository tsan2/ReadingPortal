package ru.anastasya.readingportal.exceptions;

public class TokenExpiredException extends TokenValidationException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
