package ru.anastasya.readingportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="password_reset_codes")
public class PasswordResetCode {

    @Id
    @SequenceGenerator(name = "password_seq", sequenceName = "password_sequence", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_seq")
    private Long id;
    private String code;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetCode(User user, String code, LocalDateTime expiresAt) {
        this.id = null;
        this.user = user;
        this.code = code;
        this.expiresAt = expiresAt;
    }

}
