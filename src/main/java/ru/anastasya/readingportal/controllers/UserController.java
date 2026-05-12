package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.mappers.UserMapper;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    //потом будет по авторизации
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePasswordByOldPassword(@RequestBody @Valid ChangePasswordByOldPasswordDTO passwordDTO) {
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
    public ResponseEntity<String> changeNickname(@RequestBody @Valid ChangeNicknameDTO nicknameDTO) {
        userService.changeNickname(nicknameDTO);
        return new ResponseEntity<>("Успешно", HttpStatus.OK);
    }

    //потом будет по авторизации
    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody @Valid ChangeEmailDTO emailDTO){
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

        ProfileDTO userProfile = userMapper.toProfileDTO(user);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping(params = "nickname")
    public ResponseEntity<?> getInfoUserByNickname(@RequestParam String nickname) {
        User user = userService.findUserByNickname(nickname);
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInfoUser(@PathVariable Long id){
        User user = userService.findById(id);
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
