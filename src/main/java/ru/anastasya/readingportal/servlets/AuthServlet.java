package ru.anastasya.readingportal.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.anastasya.readingportal.dto.ForgotPasswordDTO;
import ru.anastasya.readingportal.dto.ResetPasswordDTO;
import ru.anastasya.readingportal.dto.UserLoginDTO;
import ru.anastasya.readingportal.dto.UserRegisterDTO;
import ru.anastasya.readingportal.exception.AuthenticationException;
import ru.anastasya.readingportal.exception.ConflictException;
import ru.anastasya.readingportal.exception.RegistrationException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.EmailService;
import ru.anastasya.readingportal.services.PasswordResetCodeService;
import ru.anastasya.readingportal.services.UserService;
import ru.anastasya.readingportal.utils.CodeGenerator;
import ru.anastasya.readingportal.utils.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private static final UserService userService = UserService.getInstance();
    private static final ObjectMapper jsonMapper = JsonUtil.getInstance();
    private static final PasswordResetCodeService resetCodeService = PasswordResetCodeService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String pathInfo = req.getPathInfo();
        switch (pathInfo) {
            case "/register" -> register(req, resp);
            case "/login" -> login(req, resp);
            case "/logout" -> logout(req, resp);
            case "/forgot-password" -> forgotPassword(req, resp);
            case "/reset-password" -> resetPassword(req, resp);
            default -> sendJsonError(resp, resp.getWriter(), HttpServletResponse.SC_NOT_FOUND, "Ресурс не найден");
        }

    }

    //написать возврат какого то результата json
    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json, charset=UTF-8");
        PrintWriter respWriter = resp.getWriter();

        BufferedReader reader = req.getReader();
        UserRegisterDTO userDTO = null;
        try{
            userDTO = jsonMapper.readValue(reader, UserRegisterDTO.class);
        } catch (IOException e) {
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Отправлен некорректный json");
            return;
        }

        if (userDTO.nickname() == null || userDTO.nickname().isBlank()){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Никнейм не может быть пустым");
            return;
        }
        if (userDTO.email() == null || userDTO.email().isBlank()){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Почта не может быть пустой");
            return;
        }
        if (userDTO.password() == null || userDTO.password().isBlank()){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Пароль не может быть пустым");
            return;
        }
        if (userDTO.nickname().length()>30){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Слишком длинный никнейм");
            return;
        }
        if (!userDTO.email().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$")){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Неверный формат почты");
            return;
        }


        User user = new User(userDTO.nickname(), userDTO.email(), userDTO.password());
        try {
            userService.registerUser(user);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            jsonMapper.writeValue(respWriter, Map.of("success", true));
        } catch (ValidationException e){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ConflictException e){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }


    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();

        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter respWriter = resp.getWriter();

        BufferedReader reqReader = req.getReader();
        UserLoginDTO userLoginDTO = null;
        try {
             userLoginDTO = jsonMapper.readValue(reqReader, UserLoginDTO.class);
        } catch (IOException e) {
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Отправлен некорректный json");
            return;
        }

        if (userLoginDTO.emailOrNickname() == null || userLoginDTO.emailOrNickname().isBlank()){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Почта или никнейм не могут быть пустыми");
            return;
        }
        if (userLoginDTO.password() == null || userLoginDTO.password().isBlank()){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Пароль не может быть пустым");
            return;
        }

        try {
            User user = userService.authorizationUser(userLoginDTO.emailOrNickname(), userLoginDTO.password());

            req.changeSessionId();
            session.setAttribute("current_user", user);

            resp.setStatus(HttpServletResponse.SC_OK);
            jsonMapper.writeValue(respWriter, Map.of("current_id", user.getId()));
        } catch (AuthenticationException e){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json; charset=UTF-8");

        HttpSession session = req.getSession(false);
        if(session != null){
            session.invalidate();
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        jsonMapper.writeValue(resp.getWriter(), Map.of("success", true));

    }

    private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter respWriter = resp.getWriter();

        ForgotPasswordDTO passwordDTO = null;
        try {
            passwordDTO = jsonMapper.readValue(req.getReader(), ForgotPasswordDTO.class);
        } catch (IOException e) {
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Некорректный json");
        }

        resetCodeService.sendCode(passwordDTO.email());

        resp.setStatus(HttpServletResponse.SC_OK);
        jsonMapper.writeValue(respWriter, Map.of("success", true));

    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter respWriter = resp.getWriter();

        ResetPasswordDTO resetPasswordDTO = null;

        try {
            resetPasswordDTO = jsonMapper.readValue(req.getReader(), ResetPasswordDTO.class);
        } catch (IOException e) {
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Некорректный json");
        }

        try {
            userService.changePassword(resetPasswordDTO.email(), resetPasswordDTO.code(), resetPasswordDTO.newPassword());
        } catch (ValidationException e){
            sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        jsonMapper.writeValue(respWriter, Map.of("success", true));
    }

    private void sendJsonError(HttpServletResponse resp, PrintWriter responseWriter, int status, String message) throws IOException {
        resp.setStatus(status);
        jsonMapper.writeValue(responseWriter, Map.of("error", message));

    }
}
