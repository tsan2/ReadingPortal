package ru.anastasya.readingportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserPublicInfoDTO(Long id,
                                String nickname,
                                LocalDateTime created_at) {
}
