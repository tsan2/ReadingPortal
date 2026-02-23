package ru.anastasya.readingportal.models;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String nickname;
    private String email;
    private String password_hash;
    private LocalDateTime created_at;

    public User() {

    }

    public User(Long id, String nickname, String email, LocalDateTime created_at) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.created_at = created_at;
    }

    public User(String nickname, String email, String password_hash, LocalDateTime created_at) {
        this.id = null;
        this.nickname = nickname;
        this.email = email;
        this.password_hash = password_hash;
        this.created_at = created_at;
    }

    public User(Long id, String nickname, String email, String password_hash, LocalDateTime created_at) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password_hash = password_hash;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", created_at=" + created_at +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
