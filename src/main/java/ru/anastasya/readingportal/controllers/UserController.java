package ru.anastasya.readingportal.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
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
    @PatchMapping("me/password")
    public ResponseEntity<MessageResponse> changePasswordByOldPassword(@RequestBody @Valid ChangePasswordByOldPasswordDTO passwordDTO) {
        userService.changePassword(passwordDTO);

        return new ResponseEntity<>(new MessageResponse("Пароль изменен"), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<UserSummaryDTO>> getAllUser(@RequestParam int page, @RequestParam int size) {
        Page<UserSummaryDTO> users = userService.findAllUser(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //потом будет по авторизации
    @PatchMapping("me/nickname")
    public ResponseEntity<MessageResponse> changeNickname(@RequestBody @Valid ChangeNicknameDTO nicknameDTO) {
        userService.changeNickname(nicknameDTO);
        return new ResponseEntity<>(new MessageResponse("Никнейм изменен"), HttpStatus.OK);
    }

    //потом будет по авторизации
    @PatchMapping("me/email")
    public ResponseEntity<MessageResponse> changeEmail(@RequestBody @Valid ChangeEmailDTO emailDTO){
        userService.changeEmail(emailDTO);
        return new ResponseEntity<>(new MessageResponse("Емейл изменен"), HttpStatus.OK);
    }

    //временная реализация пока нет авторизации
    @GetMapping("/profile/{id}")
    private ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        User user = userService.findById(id);

        ProfileDTO userProfile = userMapper.toProfileDTO(user);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping(params = "nickname")
    public ResponseEntity<UserPublicInfoDTO> getInfoUserByNickname(@RequestParam String nickname) {
        User user = userService.findUserByNickname(nickname);
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicInfoDTO> getInfoUser(@PathVariable Long id){
        User user = userService.findById(id);
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

}
