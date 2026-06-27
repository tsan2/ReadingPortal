package ru.anastasya.readingportal.exceptions;

public class TokenInvalidSignatureException extends TokenValidationException {
    public TokenInvalidSignatureException(String message) {
        super(message);
    }
}
