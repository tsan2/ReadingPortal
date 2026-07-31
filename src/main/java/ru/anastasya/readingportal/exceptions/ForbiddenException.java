package ru.anastasya.readingportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends ServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
