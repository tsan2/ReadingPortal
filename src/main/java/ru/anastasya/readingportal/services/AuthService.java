package ru.anastasya.readingportal.services;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.JwtFullResponse;
import ru.anastasya.readingportal.dto.JwtShortResponse;
import ru.anastasya.readingportal.dto.RefreshTokenResult;
import ru.anastasya.readingportal.dto.UserLoginDTO;
import ru.anastasya.readingportal.exceptions.AuthenticationException;
import ru.anastasya.readingportal.exceptions.TokenValidationException;
import ru.anastasya.readingportal.models.RefreshToken;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.RefreshTokenRepository;
import ru.anastasya.readingportal.security.JwtProvider;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JwtFullResponse login(UserLoginDTO dto){
        User user = userService.findByEmailOrNickname(dto.emailOrNickname());
        if (user == null){
            throw new AuthenticationException("Логин или пароль введен неверно");
        }
        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())){
            throw new AuthenticationException("Логин или пароль введен неверно");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        RefreshTokenResult refreshTokenResult = jwtProvider.generateRefreshToken(user);

        RefreshToken token = new RefreshToken(refreshTokenResult.idToken(),
                user, refreshTokenResult.expiresAt());
        refreshTokenRepository.save(token);

        return new JwtFullResponse(accessToken, refreshTokenResult.refreshToken());
    }

    @Transactional
    public JwtFullResponse refreshToken(String refreshToken){

        if (!jwtProvider.validateToken(refreshToken)){
            throw new TokenValidationException("Токен невалиден");
        }
        Claims claims = jwtProvider.getClaims(refreshToken);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(UUID.fromString(claims.getId()));
        System.out.println(claims.getId());
        System.out.println(claims.get("userId"));
        if (refreshTokenOptional.isEmpty()){
            refreshTokenRepository.deleteAllByUserId(Long.valueOf(claims.get("userId").toString()));
            throw new TokenValidationException("Токен невалиден");
        }
        RefreshToken refreshTokenObject = refreshTokenOptional.get();
        User user = userService.findById(refreshTokenObject.getUser().getId());

        String newAccessToken = jwtProvider.generateAccessToken(user);
        RefreshTokenResult newRefreshTokenResult = jwtProvider.generateRefreshToken(user);
        RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenResult.idToken(),
                user, newRefreshTokenResult.expiresAt());

        refreshTokenRepository.save(newRefreshToken);
        refreshTokenRepository.delete(refreshTokenObject);

        return new JwtFullResponse(newAccessToken, newRefreshTokenResult.refreshToken());
    }

    @Transactional
    public void logout(String refreshToken){
        if (!jwtProvider.validateToken(refreshToken)){
            throw new TokenValidationException("Токен невалиден");
        }
        Claims claims = jwtProvider.getClaims(refreshToken);
        refreshTokenRepository.deleteById(UUID.fromString(claims.getId()));
    }
}
