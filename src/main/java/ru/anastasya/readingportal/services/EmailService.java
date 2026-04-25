package ru.anastasya.readingportal.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendCode(String email, String code){
        System.out.println("email на почту " + email + " с кодом " + code);
    }

}
