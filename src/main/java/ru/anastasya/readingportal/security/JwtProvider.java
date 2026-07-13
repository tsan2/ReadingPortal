package ru.anastasya.readingportal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.anastasya.readingportal.dto.RefreshTokenResult;
import ru.anastasya.readingportal.models.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final SecretKey jwtSecretKey;

    public JwtProvider(@Value("${jwt.secret}") String jwtSecretKey){
        this.jwtSecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretKey));
    }

    public String generateAccessToken(User user){
        Instant issuedTime = Instant.now();
        Instant expirationTime = issuedTime.plus(5, ChronoUnit.MINUTES);
        return Jwts.builder()
                .expiration(Date.from(expirationTime))
                .issuedAt(Date.from(issuedTime))
                .subject(user.getNickname())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .signWith(jwtSecretKey)
                .compact();
    }

    public RefreshTokenResult generateRefreshToken(User user){
        Instant issuedTime = Instant.now();
        Instant expirationTime = issuedTime.plus(30, ChronoUnit.DAYS);
        UUID idToken = UUID.randomUUID();
        String refreshToken = Jwts.builder()
                .subject(user.getNickname())
                .expiration(Date.from(expirationTime))
                .issuedAt(Date.from(issuedTime))
                .id(idToken.toString())
                .claim("userId", user.getId())
                .signWith(jwtSecretKey)
                .compact();
        return new RefreshTokenResult(refreshToken, idToken, Date.from(expirationTime));
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}
