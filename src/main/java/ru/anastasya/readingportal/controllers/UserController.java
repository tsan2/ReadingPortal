package ru.anastasya.readingportal.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exception.ConflictException;
import ru.anastasya.readingportal.exception.EntityNotFoundException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    //потом будет по авторизации
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePasswordByOldPassword(@RequestBody ChangePasswordByOldPasswordDTO passwordDTO) {
        userService.changePassword(passwordDTO);
        return new ResponseEntity<>("Пароль изменен", HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<UserSummaryDTO>> getAllUser(@RequestParam int page, @RequestParam int size) {
        Page<UserSummaryDTO> users = userService.findAllUser(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //потом будет по авторизации
    @PatchMapping("/change-nickname")
    public ResponseEntity<String> changeNickname(@RequestBody ChangeNicknameDTO nicknameDTO) {
        userService.changeNickname(nicknameDTO);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    //потом будет по авторизации
    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailDTO emailDTO){
        userService.changeEmail(emailDTO);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    //временная реализация пока нет авторизации
    @GetMapping("/profile/{id}")
    private ResponseEntity<?> getProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        ProfileDTO userProfile = new ProfileDTO(user.getId(), user.getNickname(),
                user.getEmail(), user.getCreatedAt(), user.getVersion());

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping(params = "nickname")
    public ResponseEntity<?> getInfoUserByNickname(@RequestParam String nickname) {
        User user = userService.findUserByNickname(nickname);
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        UserPublicInfoDTO userInfo = new UserPublicInfoDTO(user.getId(), user.getNickname(), user.getCreatedAt());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInfoUser(@PathVariable Long id){
        User user = userService.findById(id);
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        UserPublicInfoDTO userInfo = new UserPublicInfoDTO(user.getId(), user.getNickname(), user.getCreatedAt());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
