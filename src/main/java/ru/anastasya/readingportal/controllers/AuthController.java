package ru.anastasya.readingportal.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anastasya.readingportal.dto.UserRegisterDTO;
import ru.anastasya.readingportal.exception.ConflictException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthController(UserService userService){
        this.userService = userService;
    }

    private final UserService userService;

    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody UserRegisterDTO userDTO){
        if (userDTO.nickname() == null || userDTO.nickname().isBlank()){
            return new ResponseEntity<>("Никнейм не может быть пустым", HttpStatus.BAD_REQUEST);
        }
        if (userDTO.email() == null || userDTO.email().isBlank()){
            return new ResponseEntity<>("Почта не может быть пустой", HttpStatus.BAD_REQUEST);
        }
        if (userDTO.password() == null || userDTO.password().isBlank()){
            return new ResponseEntity<>("Пароль не может быть пустым", HttpStatus.BAD_REQUEST);
        }
        if (userDTO.nickname().length()>30){
            return new ResponseEntity<>("Слишком длинный никнейм", HttpStatus.BAD_REQUEST);
        }
        if (!userDTO.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$")){
            return new ResponseEntity<>("Неверный формат почты", HttpStatus.BAD_REQUEST);
        }


        User user = new User(userDTO.nickname(), userDTO.email(), userDTO.password());

        userService.registerUser(user);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);

    }
}
