package ru.anastasya.readingportal.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anastasya.readingportal.models.PasswordResetCode;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.repositories.PasswordResetCodeRepository;
import ru.anastasya.readingportal.repositories.UserRepository;
import ru.anastasya.readingportal.utils.CodeGenerator;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class PasswordResetCodeService {

    private final PasswordResetCodeRepository resetCodeRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;


    @Transactional
    public void sendCode(String email){
        User user = userRepository.findByEmail(email);

        if (user==null){
            return;
        }

        String code = CodeGenerator.generateCode();
        PasswordResetCode resetCode = new PasswordResetCode(user, code, LocalDateTime.now().plusMinutes(10));

        resetCodeRepository.save(resetCode);

        emailService.sendCode(email, code);
    }

    @Transactional(readOnly = true)
    public boolean validCode(Long userId, String code){
        PasswordResetCode resetCode = resetCodeRepository.findValidCode(userId, code);
        return resetCode != null;
    }

    @Transactional
    public void deleteCodes(Long userId){
        resetCodeRepository.deleteByUserId(userId);
    }
}
