package ru.anastasya.readingportal.services;

public class EmailService {

    public void sendCode(String email, String code){
        System.out.println("email на почту " + email + " с кодом " + code);
    }

}
