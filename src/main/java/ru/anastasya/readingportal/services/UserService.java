package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.UserDAO;
import ru.anastasya.readingportal.dto.UserSummaryDTO;
import ru.anastasya.readingportal.exception.AuthorizationException;
import ru.anastasya.readingportal.exception.RegistrationException;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.PasswordUtil;

import java.util.List;

public class UserService {

    private final static UserService INSTANCE = new UserService();

    private UserService(){}

    public static UserService getInstance(){
        return INSTANCE;
    }

    private final UserDAO userDAO = UserDAO.getInstance();
    private final PasswordResetCodeService resetCodeService = PasswordResetCodeService.getInstance();
    private final BookService bookService = BookService.getInstance();


    public void registerUser(User user){
        validateUser(user);
        if (user.getNickname().length()>30){
            throw new RegistrationException("Слишком длинный никнейм");
        }
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$")){
            throw new RegistrationException("Это не адрес электронной почты");
        }
        if (userDAO.existsEmail(user.getEmail())){
            throw new RegistrationException("Аккаунт с такой почтой уже существует");
        }
        if (userDAO.existsNickname(user.getNickname())){
            throw new RegistrationException("Аккаунт с таким никнеймом уже существует");
        }

        String hashPassword = PasswordUtil.hashPassword(user.getPasswordHash());
        user.setPasswordHash(hashPassword);

        userDAO.save(user);
    }

    public User authorizationUser(String emailOrNickname, String password){

        User user = userDAO.findFullUserByEmailOrNickname(emailOrNickname);
        if (user == null){
            throw new AuthorizationException("Неверная почта или пароль");
        }

        if(PasswordUtil.checkPassword(password, user.getPasswordHash())){
            user.setPasswordHash(null);
            return user;
        }
        else{
            throw new AuthorizationException("Неверная почта или пароль");
        }

    }


    public void changePassword(Long id, String oldPassword, String newPassword){

        User user = userDAO.findFullUserById(id);
        if (PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())){
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePasswordHash(id, newPasswordHash);
        }
        else{
            throw new ServiceException("Старый пароль введен неверно");
        }

    }

    public void changePassword(String email, String code, String newPassword){

        User user = userDAO.findByEmail(email);

        if (user==null){
            throw new ServiceException("Неверный код или почта");
        }

        Long UserId = user.getId();

        if(!resetCodeService.validCode(UserId, code)){
            throw new ServiceException("Неверный код или почта");
        }

        resetCodeService.deleteCodes(UserId);

        String hashPassword = PasswordUtil.hashPassword(newPassword);

        userDAO.updatePasswordHash(user.getId(), hashPassword);

    }

    public void changeNickname(Long id, String newNickname){
        User user = userDAO.findById(id);
        if (user==null){
            throw new ServiceException("Пользователь не найден");
        }
        if (user.getNickname().equals(newNickname)){
            return;
        }
        if (userDAO.existsNickname(newNickname)){
            throw new ServiceException("Такой никнейм уже существует");
        }
        user.setNickname(newNickname);
        userDAO.update(user);
    }

    public void changeEmail(Long id, String password, String newEmail){
        User user = userDAO.findFullUserById(id);
        if (user==null){
            throw new ServiceException("Пользователь не найден");
        }
        if (user.getEmail().equals(newEmail)){
            return;
        }
        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())){
            throw new ServiceException("Пароль неверный");
        }
        if (userDAO.existsEmail(newEmail)){
            throw new ServiceException("Такой адрес электронной почты уже занят");
        }

        user.setEmail(newEmail);
        userDAO.update(user);
    }

    public List<UserSummaryDTO> findAllUser(int page, int size){
        int offset = (page-1)*size;
        List<User> users = userDAO.findAll(size, offset);
        List<UserSummaryDTO> userSummaryDTOS = users.stream()
                .map(u -> new UserSummaryDTO(u.getId(), u.getNickname()))
                .toList();

        return userSummaryDTOS;
    }

    public User findById(Long id){
        return userDAO.findById(id);
    }

    public User findUserByNickname(String Nickname){
        return userDAO.findByNickname(Nickname);
    }

    public void deleteUser(Long id, boolean deleteBookOrNo){
        if (deleteBookOrNo){
            bookService.deleteAllBookByUserId(id);
        }
        userDAO.delete(id);

    }

    private void validateUser(User user){
        if (user.getNickname() == null || user.getNickname().isBlank()){
            throw new ServiceException("Никнейм не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()){
            throw new ServiceException("Почта не может быть пустой");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()){
            throw new ServiceException("Пароль не может быть пустым");
        }
    }

}
