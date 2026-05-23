package ru.anastasya.readingportal.dto;

import java.time.LocalDateTime;

public record UserPublicInfoDTO(Long id,
                                String nickname,
                                LocalDateTime createdAt) {
}
