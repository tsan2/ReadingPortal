package ru.anastasya.readingportal.models;

import java.time.LocalDateTime;

public class PasswordResetCode {

    private Long id;
    private Long user_id;
    private String code;
    private LocalDateTime expires_at;
    private LocalDateTime created_at;

    public PasswordResetCode(Long user_id, String code, LocalDateTime expires_at) {
        this.id = null;
        this.user_id = user_id;
        this.code = code;
        this.expires_at = expires_at;
    }

    public PasswordResetCode(Long id, Long user_id, String code, LocalDateTime expires_at, LocalDateTime created_at) {
        this.id = id;
        this.user_id = user_id;
        this.code = code;
        this.expires_at = expires_at;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PasswordResetCode{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", code='" + code + '\'' +
                ", expires_at=" + expires_at +
                ", created_at=" + created_at +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(LocalDateTime expires_at) {
        this.expires_at = expires_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
