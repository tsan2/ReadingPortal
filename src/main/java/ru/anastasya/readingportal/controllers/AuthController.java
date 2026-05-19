package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anastasya.readingportal.dto.ForgotPasswordDTO;
import ru.anastasya.readingportal.dto.ResetPasswordDTO;
import ru.anastasya.readingportal.dto.UserRegisterDTO;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.PasswordResetCodeService;
import ru.anastasya.readingportal.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetCodeService resetCodeService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }

        userService.registerUser(userDTO);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordDTO passwordDTO){
        resetCodeService.sendCode(passwordDTO.email());

        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){

        userService.changePassword(resetPasswordDTO);

        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }
}
