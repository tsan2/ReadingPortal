package ru.anastasya.readingportal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    private static final ObjectMapper INSTANCE = new ObjectMapper().findAndRegisterModules();

    public static ObjectMapper getInstance(){
        return INSTANCE;
    }

}
