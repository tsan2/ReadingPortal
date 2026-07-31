package ru.anastasya.readingportal.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.anastasya.readingportal.dto.ErrorResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Вы не авторизованы", OffsetDateTime.now());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
