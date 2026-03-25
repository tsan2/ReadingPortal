package ru.anastasya.readingportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserPublicInfoDTO(Long id,
                                String nickname,
                                String email,
                                @JsonFormat(pattern = "dd/MM/yyyy, HH:mm")
                                LocalDateTime created_at) {
}
