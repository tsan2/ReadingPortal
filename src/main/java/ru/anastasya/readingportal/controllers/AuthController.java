package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.ConflictException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.PasswordResetCodeService;
import ru.anastasya.readingportal.services.UserService;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetCodeService resetCodeService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> register(@RequestBody @Valid UserRegisterDTO userDTO){
        ProfileDTO user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid ForgotPasswordDTO passwordDTO){
        resetCodeService.sendCode(passwordDTO.email());

        MessageResponse messageResponse = new MessageResponse("""
                Если пользователь с такой почтой зарегистрирован,
                код отправлен на почту""");
        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        userService.changePassword(resetPasswordDTO);

        MessageResponse messageResponse = new MessageResponse("Пароль изменен");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
