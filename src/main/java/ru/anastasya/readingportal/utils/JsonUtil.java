package ru.anastasya.readingportal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper().findAndRegisterModules();

        INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper getInstance(){
        return INSTANCE;
    }

    public static void sendJsonError(HttpServletResponse resp, PrintWriter responseWriter, int status, String message) throws IOException {
        resp.setStatus(status);
        INSTANCE.writeValue(responseWriter, Map.of("error", message));

    }

}
