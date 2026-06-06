package ru.anastasya.readingportal.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exceptions.*;
import ru.anastasya.readingportal.mappers.UserMapper;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.UserRepository;

@Service
public class UserService {

    public UserService(UserRepository userRepository, PasswordResetCodeService resetCodeService,
                       UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resetCodeService = resetCodeService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private final UserRepository userRepository;
    private final PasswordResetCodeService resetCodeService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public ProfileDTO registerUser(UserRegisterDTO userRegisterDTO){
        User user = userMapper.fromUserRegisterDTO(userRegisterDTO);
        String hashPassword = passwordEncoder.encode(userRegisterDTO.password());
        user.setPasswordHash(hashPassword);

        if (userRepository.existsByEmail(user.getEmail())){
            throw new ConflictException("Аккаунт с такой почтой уже существует");
        }
        if (userRepository.existsByNickname(user.getNickname())){
            throw new ConflictException("Аккаунт с таким никнеймом уже существует");
        }

        User newUser = userRepository.save(user);
        return userMapper.toProfileDTO(newUser);
    }

    @Transactional(readOnly = true)
    public ProfileDTO login(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return userMapper.toProfileDTO(user);
    }

//    @Transactional(readOnly = true)
//    public User authorizationUser(String emailOrNickname, String password){
//
//        User user = userRepository.findByEmailOrNickname(emailOrNickname);
//        if (user == null){
//            throw new AuthenticationException("Неверный логин или пароль");
//        }
//
//        if(passwordEncoder.matches(password, user.getPasswordHash())){
//            user.setPasswordHash(null);
//            return user;
//        }
//        else{
//            throw new AuthenticationException("Неверный логин или пароль");
//        }
//
//    }

    @Transactional
    public void changePassword(ChangePasswordByOldPasswordDTO dto){

        User user = userRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
        if (passwordEncoder.matches(dto.oldPassword(), user.getPasswordHash())){
            if (!user.getVersion().equals(dto.version())){
                throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте ещё раз");
            }
            String newPasswordHash = passwordEncoder.encode(dto.newPassword());
            user.setPasswordHash(newPasswordHash);
        }
        else{
            throw new ValidationException("Старый пароль введен неверно");
        }

    }

    @Transactional
    public void changePassword(ResetPasswordDTO dto){

        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new ValidationException("Неверный код или почта"));

        Long UserId = user.getId();

        if(!resetCodeService.validCode(UserId, dto.code())){
            throw new ValidationException("Неверный код или почта");
        }

        String hashPassword = passwordEncoder.encode(dto.newPassword());

        user.setPasswordHash(hashPassword);
        resetCodeService.deleteCodes(UserId);
    }

    @Transactional
    public void changeNickname(ChangeNicknameDTO dto, Long currentUserId){
        User user = userRepository.findById(currentUserId).orElse(null);
        if (user==null){
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (user.getNickname().equals(dto.newNickname())){
            return;
        }
        if (userRepository.existsByNickname(dto.newNickname())){
            throw new ConflictException("Такой никнейм уже существует");
        }
        if (!user.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте снова");
        }
        user.setNickname(dto.newNickname());
    }

    @Transactional
    public void changeEmail(ChangeEmailDTO dto, Long currentUserId){
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        if (user.getEmail().equals(dto.newEmail())){
            return;
        }
        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())){
            throw new ValidationException("Пароль неверный");
        }
        if (userRepository.existsByEmail(dto.newEmail())){
            throw new ConflictException("Такой адрес электронной почты уже занят");
        }
        if (!user.getVersion().equals(dto.version())){
            throw new OptimisticLockException("Кто-то уже изменил данные. Попробуйте снова");
        }
        user.setEmail(dto.newEmail());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserSummaryDTO> findAllUser(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> users = userRepository.findAll(pageable);
        Page<UserSummaryDTO> userSummaryDTOS = users.map(userMapper::toUserSummaryDTO);

        return userSummaryDTOS;
    }

    @Transactional(readOnly = true)
    public long countAllUser(){
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    public User findUserByNickname(String nickname){
        return userRepository.findByNickname(nickname).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
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



}