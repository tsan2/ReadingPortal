package ru.anastasya.readingportal.services;

import ru.anastasya.readingportal.dao.PasswordResetCodeDAO;
import ru.anastasya.readingportal.dao.UserDAO;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.models.PasswordResetCode;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.utils.CodeGenerator;

import java.time.LocalDateTime;

public class PasswordResetCodeService {

    private final PasswordResetCodeDAO resetCodeDAO;
    private final EmailService emailService;
    private final UserDAO userDAO;

    public PasswordResetCodeService(PasswordResetCodeDAO resetCodeDAO, EmailService emailService, UserDAO userDAO){
        this.resetCodeDAO = resetCodeDAO;
        this.emailService = emailService;
        this.userDAO = userDAO;
    }

    public void sendCode(String email){
        User user = userDAO.findByEmail(email);

        if (user==null){
            return;
        }

        String code = CodeGenerator.generateCode();
        PasswordResetCode resetCode = new PasswordResetCode(user.getId(), code, LocalDateTime.now().plusMinutes(10));

        resetCodeDAO.saveCode(resetCode);

        emailService.sendCode(email, code);
    }

    public boolean validCode(Long userId, String code){
        PasswordResetCode resetCode = resetCodeDAO.findValidCode(userId, code);
        return resetCode != null;
    }

    public void deleteCodes(Long userId){
        resetCodeDAO.deleteAllCodesByUserId(userId);
    }

}
