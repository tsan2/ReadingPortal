package ru.anastasya.readingportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProfileDTO(Long id,
                         String nickname,
                         String email,
                         LocalDateTime created_at) {
}
