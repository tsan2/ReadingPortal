package ru.anastasya.readingportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ServiceException {
    public ConflictException(String message) {
        super(message);
    }
}
