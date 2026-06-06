package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.ConflictException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.security.CustomUserDetails;
import ru.anastasya.readingportal.services.PasswordResetCodeService;
import ru.anastasya.readingportal.services.UserService;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetCodeService resetCodeService;

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
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@AuthenticationPrincipal CustomUserDetails userDetails){
        ProfileDTO profileDTO = userService.login(userDetails.getId());
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
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
