package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @SequenceGenerator(name="user_seq", sequenceName = "user_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return Objects.equals(user.getEmail(), this.getEmail());
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }

    public User(Long id, String nickname, String email, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
    }

    public User(String nickname, String email, String passwordHash) {
        this.id = null;
        this.nickname = nickname;
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
