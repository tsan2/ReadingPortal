package ru.anastasya.readingportal.services;

public class EmailService {

    private final static EmailService INSTANCE = new EmailService();

    private EmailService(){}

    public static EmailService getInstance(){
        return INSTANCE;
    }

    public void sendCode(String email, String code){
        System.out.println("email на почту " + email + " с кодом " + code);
    }

}
