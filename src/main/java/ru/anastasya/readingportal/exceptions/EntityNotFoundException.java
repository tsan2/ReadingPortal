package ru.anastasya.readingportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
