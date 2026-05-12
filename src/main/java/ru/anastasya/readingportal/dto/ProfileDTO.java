package ru.anastasya.readingportal.dto;

import java.time.LocalDateTime;

public record ProfileDTO(Long id,
                         String nickname,
                         String email,
                         LocalDateTime createdAt,
                         Integer version) {
}
