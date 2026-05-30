package ru.anastasya.readingportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Schema(description = "Ответ, содержащий сообщение")
@Getter
public class MessageResponse{
    @Schema(description = "сообщение", example = "успешно")
    private String message;
    @Schema(description = "время запроса", example = "2026-05-29T19:38:51.7530871+05:00")
    private OffsetDateTime timestamp;

    public MessageResponse(String message){
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }
}
