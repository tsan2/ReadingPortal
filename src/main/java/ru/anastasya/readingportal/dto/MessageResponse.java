package ru.anastasya.readingportal.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
public class MessageResponse{
    private String message;
    private OffsetDateTime timestamp;

    public MessageResponse(String message){
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }
}
