package ru.anastasya.readingportal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.mappers.UserMapper;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.security.CustomUserDetails;
import ru.anastasya.readingportal.services.UserService;

@Tag(name = "Пользователи",
description = "Методы для работы с пользователями")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    //потом будет по авторизации
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные")
    @Operation(summary = "Сменить пароль по старому паролю")
    @PatchMapping("/me/password")
    public ResponseEntity<MessageResponse> changePasswordByOldPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @RequestBody @Valid ChangePasswordByOldPasswordDTO passwordDTO) {
        userService.changePassword(passwordDTO, userDetails.getId());

        return new ResponseEntity<>(new MessageResponse("Пароль изменен"), HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Получить список пользователей")
    @GetMapping("")
    public ResponseEntity<Page<UserSummaryDTO>> getAllUser(@Parameter(description = "номер страницы (начинается с 1)", example = "1")
                                                           @RequestParam @Min(1) int page,
                                                           @Parameter(description = "размер страницы", example = "10")
                                                           @RequestParam @Min(1) int size) {
        Page<UserSummaryDTO> users = userService.findAllUser(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //потом будет по авторизации
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @Operation(summary = "Сменить никнейм")
    @PatchMapping("/me/nickname")
    public ResponseEntity<MessageResponse> changeNickname(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody @Valid ChangeNicknameDTO nicknameDTO) {
        userService.changeNickname(nicknameDTO, userDetails.getId());
        return new ResponseEntity<>(new MessageResponse("Никнейм изменен"), HttpStatus.OK);
    }

    //потом будет по авторизации
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос или неверный пароль")
    @ApiResponse(responseCode = "409", description = "Кто-то уже изменил данные или емейл занят")
    @Operation(summary = "Сменить адрес электронной почты")
    @PatchMapping("/me/email")
    public ResponseEntity<MessageResponse> changeEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody @Valid ChangeEmailDTO emailDTO){
        userService.changeEmail(emailDTO, userDetails.getId());
        return new ResponseEntity<>(new MessageResponse("Емейл изменен"), HttpStatus.OK);
    }

    //временная реализация пока нет авторизации
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @Operation(summary = "Получить свой профиль")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findById(userDetails.getId());

        ProfileDTO userProfile = userMapper.toProfileDTO(user);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @Operation(summary = "Найти пользователя по никнейму")
    @GetMapping(value = "/search", params = "nickname")
    public ResponseEntity<UserPublicInfoDTO> getInfoUserByNickname(@Parameter(name = "никнейм", example = "tsan")
                                                                   @RequestParam @NotBlank String nickname) {
        User user = userService.findUserByNickname(nickname);
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }


    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @Operation(summary = "Найти пользователя по айди")
    @GetMapping("/{id}")
    public ResponseEntity<UserPublicInfoDTO> getInfoUser(@Parameter(description = "айди пользователя", example = "1")
                                                         @PathVariable @Min(1) Long id){
        User user = userService.findById(id);
        UserPublicInfoDTO userInfo = userMapper.toUserPublicInfoDTO(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

}
