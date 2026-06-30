package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Parent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.services.AuthService;
import ru.anastasya.readingportal.services.PasswordResetCodeService;
import ru.anastasya.readingportal.services.UserService;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetCodeService resetCodeService;
    private final AuthService authService;

    @Tag(name = "Авторизация", description = "Методы для работы с регистрацией и авторизацией пользователей")
    @Operation(summary = "Зарегистрироваться")
    @ApiResponse(responseCode = "201", description = "Объект успешно создан")
    @ApiResponse(responseCode = "404", description = "Аккаунт с такой почтой или никнеймом уже существует")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> register(@RequestBody @Valid UserRegisterDTO userDTO){
        ProfileDTO user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Tag(name = "Авторизация", description = "Методы для работы с регистрацией и авторизацией пользователей")
    @Operation(summary = "Войти в аккаунт")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "401", description = "Логин или пароль введен неверно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/login")
    public ResponseEntity<JwtShortResponse> login(@RequestBody @Valid UserLoginDTO userLoginDTO,
                                                  HttpServletResponse httpServletResponse){
        JwtFullResponse jwtFullResponse = authService.login(userLoginDTO);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtFullResponse.refreshToken())
                .maxAge(60 * 60 * 24 * 30)
                .httpOnly(true)
                .sameSite("Strict")
                .secure(false)  //временно
                .path("/reading-portal/auth")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(new JwtShortResponse(jwtFullResponse.accessToken()), HttpStatus.OK);
    }

    @Tag(name = "Авторизация", description = "Методы для работы с регистрацией и авторизацией пользователей")
    @Operation(summary = "Обновление токенов")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "401", description = "Токен невалиден или вы не авторизованы")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Parameter(hidden = true)
                                                        @CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                    HttpServletResponse httpServletResponse){
        if (refreshToken == null){
            HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
            ErrorResponse errorResponse = new ErrorResponse(httpStatus,
                    "Вы не авторизованы", OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, httpStatus);
        }
        JwtFullResponse jwtFullResponse = authService.refreshToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtFullResponse.refreshToken())
                .maxAge(60 * 60 * 24 * 30)
                .httpOnly(true)
                .sameSite("Strict")
                .secure(false)  //временно
                .path("/reading-portal/auth")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(new JwtShortResponse(jwtFullResponse.accessToken()), HttpStatus.OK);
    }

    @Tag(name = "Авторизация", description = "Методы для работы с регистрацией и авторизацией пользователей")
    @Operation(summary = "Выход из аккаунта")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "401", description = "Токен невалиден")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@Parameter(hidden = true) @CookieValue(value = "refreshToken", required = false) String refreshToken,
                                    HttpServletResponse httpServletResponse){
        MessageResponse messageResponse = new MessageResponse("Вы успешно вышли из аккаунта");
        if (refreshToken == null){
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        authService.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .httpOnly(true)
                .sameSite("Strict")
                .secure(false)  //временно
                .path("/reading-portal/auth")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @Tag(name = "Забыл пароль", description = "Методы для восстановления пароля")
    @ApiResponse(responseCode = "202", description = "Если пользователь с такой почтой зарегистрирован, код отправлен")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Получить код для смены пароля")
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid ForgotPasswordDTO passwordDTO){
        resetCodeService.sendCode(passwordDTO.email());

        MessageResponse messageResponse = new MessageResponse("""
                Если пользователь с такой почтой зарегистрирован,
                код отправлен на почту""");
        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @Tag(name = "Забыл пароль", description = "Методы для восстановления пароля")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос, неверный код или почта")
    @Operation(summary = "Восстановить пароль")
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        userService.changePassword(resetPasswordDTO);

        MessageResponse messageResponse = new MessageResponse("Пароль изменен");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
