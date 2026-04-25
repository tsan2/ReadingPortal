package ru.anastasya.readingportal.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.anastasya.readingportal.dto.UserSummaryDTO;
import ru.anastasya.readingportal.exception.*;
import ru.anastasya.readingportal.models.PasswordResetCode;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.UserRepository;
import ru.anastasya.readingportal.utils.PasswordUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserService(UserRepository userRepository, PasswordResetCodeService resetCodeService){
        this.userRepository = userRepository;
        this.resetCodeService = resetCodeService;
    }

    private final UserRepository userRepository;
    private final PasswordResetCodeService resetCodeService;

    @Transactional
    public void registerUser(User user){
        validateUser(user);
        if (user.getNickname().length()>30){
            throw new ValidationException("Слишком длинный никнейм");
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$")){
            throw new ValidationException("Это не адрес электронной почты");
        }
        if (userRepository.existsByEmail(user.getEmail())){
            throw new ConflictException("Аккаунт с такой почтой уже существует");
        }
        if (userRepository.existsByNickname(user.getNickname())){
            throw new ConflictException("Аккаунт с таким никнеймом уже существует");
        }

        String hashPassword = PasswordUtil.hashPassword(user.getPasswordHash());
        user.setPasswordHash(hashPassword);
    }

    public User authorizationUser(String emailOrNickname, String password){

        User user = userRepository.findByEmailOrNickname(emailOrNickname);
        if (user == null){
            throw new AuthenticationException("Неверный логин или пароль");
        }

        if(PasswordUtil.checkPassword(password, user.getPasswordHash())){
            user.setPasswordHash(null);
            return user;
        }
        else{
            throw new AuthenticationException("Неверный логин или пароль");
        }

    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword){
        if (id == null){
            throw new ValidationException("id не может быть пустым");
        }
        if (newPassword == null || newPassword.isBlank()){
            throw new ValidationException("Новый пароль не может быть пустым");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        if (PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())){
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(newPasswordHash);
        }
        else{
            throw new ValidationException("Старый пароль введен неверно");
        }

    }

    @Transactional
    public void changePassword(String email, String code, String newPassword){

        User user = userRepository.findByEmail(email);

        if (user==null){
            throw new ValidationException("Неверный код или почта");
        }

        Long UserId = user.getId();

        if(!resetCodeService.validCode(UserId, code)){
            throw new ValidationException("Неверный код или почта");
        }

        String hashPassword = PasswordUtil.hashPassword(newPassword);

        user.setPasswordHash(hashPassword);
        resetCodeService.deleteCodes(UserId);
    }

    @Transactional
    public void changeNickname(Long id, String newNickname){
        if (id == null){
            throw new ValidationException("id не может быть null");
        }
        if (newNickname == null || newNickname.isBlank()){
            throw new ValidationException("никнейм не может быть пустым");
        }
        User user = userRepository.findById(id).orElse(null);
        if (user==null){
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (user.getNickname().equals(newNickname)){
            return;
        }
        if (userRepository.existsByNickname(newNickname)){
            throw new ConflictException("Такой никнейм уже существует");
        }
        user.setNickname(newNickname);
    }

    public void changeEmail(Long id, String password, String newEmail){
        User user = userRepository.findById(id).orElse(null);
        if (user==null){
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (user.getEmail().equals(newEmail)){
            return;
        }
        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())){
            throw new ValidationException("Пароль неверный");
        }
        if (userRepository.existsByEmail(newEmail)){
            throw new ConflictException("Такой адрес электронной почты уже занят");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public Page<UserSummaryDTO> findAllUser(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> users = userRepository.findAll(pageable);
        Page<UserSummaryDTO> userSummaryDTOS = users.map(u -> new UserSummaryDTO(u.getId(), u.getNickname()));

        return userSummaryDTOS;
    }

    public long countAllUser(){
        return userRepository.count();
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

//    public User findUserByTokenHash(String tokenHash){
//        return userRepository.findUserByTokenHash(tokenHash);
//    }

//    public void deleteUser(Long id, boolean deleteBookOrNo){
//        if (deleteBookOrNo){
//            bookService.deleteAllBookByUserId(id);
//        }
//        userRepository.delete(id);
//
//    }

    private void validateUser(User user){
        if (user.getNickname() == null || user.getNickname().isBlank()){
            throw new ValidationException("Никнейм не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()){
            throw new ValidationException("Почта не может быть пустой");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()){
            throw new ValidationException("Пароль не может быть пустым");
        }
    }

}