package ru.anastasya.readingportal.models;

import java.time.LocalDateTime;

public class RememberMeToken {

    private Long id;
    private Long userId;
    private String token_hash;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public RememberMeToken() {

    }

    public RememberMeToken(Long userId, String token_hash, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token_hash = token_hash;
        this.expiresAt = expiresAt;
    }

    public RememberMeToken(Long id, Long userId, String token_hash, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.token_hash = token_hash;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RememberMeToken{" +
                "id=" + id +
                ", userId=" + userId +
                ", token_hash='" + token_hash + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken_hash() {
        return token_hash;
    }

    public void setToken_hash(String token_hash) {
        this.token_hash = token_hash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
