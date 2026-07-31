package ru.anastasya.readingportal.dto;

import java.util.Date;
import java.util.UUID;

public record RefreshTokenResult(String refreshToken, UUID idToken, Date expiresAt) {
}
