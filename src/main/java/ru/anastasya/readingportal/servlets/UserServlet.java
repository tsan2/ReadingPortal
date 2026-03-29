package ru.anastasya.readingportal.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.anastasya.readingportal.dto.*;
import ru.anastasya.readingportal.exception.ConflictException;
import ru.anastasya.readingportal.exception.EntityNotFoundException;
import ru.anastasya.readingportal.exception.ServiceException;
import ru.anastasya.readingportal.exception.ValidationException;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.UserService;
import ru.anastasya.readingportal.utils.JsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private final static ObjectMapper jsonMapper = JsonUtil.getInstance();
    private final static UserService userService = UserService.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")){
            doPatch(req, resp);
        } else{
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isBlank() || pathInfo.equals("/")){
            String nickname = req.getParameter("nickname");
            if (nickname==null){
                getAllUser(req, resp);
            }
            else{
                getInfoUserByNickname(req, resp, nickname);
            }
        }
        else if (pathInfo.equals("/me")){
            getProfile(req, resp);
        }
        else{
            try{
                String num = pathInfo.replace("/", "");
                Long id = Long.parseLong(num);
                getInfoUser(req, resp, id);
            } catch (NumberFormatException e) {
                resp.setContentType("application/json; charset=UTF-8");
                JsonUtil.sendJsonError(resp, resp.getWriter(), HttpServletResponse.SC_NOT_FOUND, "Ресурс не найден");
            }

        }



    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else if (pathInfo.equals("/me/change-nickname")){
            changeNickname(req, resp);
        }
        else if (pathInfo.equals("/me/change-password")){
            changePasswordByOldPassword(req, resp);
        }
        else {
            resp.setContentType("application/json; charset=UTF-8");
            JsonUtil.sendJsonError(resp, resp.getWriter(), HttpServletResponse.SC_NOT_FOUND, "Ресурс не найден");
        }
    }

    private void changePasswordByOldPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter respWriter = resp.getWriter();

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("current_user");
        if (user==null){
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_UNAUTHORIZED, "Необходимо авторизоваться");
            return;
        }

        ChangePasswordByOldPasswordDTO passwordDTO = null;
        try{
            passwordDTO = jsonMapper.readValue(req.getReader(), ChangePasswordByOldPasswordDTO.class);
        } catch (IOException e) {
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Отправлен некорректный json");
            return;
        }

        try {
            userService.changePassword(user.getId(),  passwordDTO.oldPassword(), passwordDTO.newPassword());
            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(jsonMapper.writeValueAsString(Map.of("success", true)));
        } catch (ValidationException e){
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void getAllUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Самое начало");
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()){
            int page = Integer.parseInt(req.getParameter("page"));
            int size = Integer.parseInt(req.getParameter("size"));

            List<UserSummaryDTO> users = userService.findAllUser(page, size);

            long totalCount = userService.countAllUser();

            PageResponseDTO<UserSummaryDTO> pageResponseDTO = new PageResponseDTO<>(users, totalCount, size, page);

            String json = jsonMapper.writeValueAsString(pageResponseDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(json);
        }

    }

    private void changeNickname(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter respWriter = resp.getWriter();

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("current_user");

        if (user==null){
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_UNAUTHORIZED, "Авторизуйтесь, чтобы сменить никнейм");
            return;
        }

        ChangeNicknameDTO nicknameDTO = null;
        try{
            nicknameDTO = jsonMapper.readValue(req.getReader(), ChangeNicknameDTO.class);
        } catch (IOException e) {
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_BAD_REQUEST, "Отправлен некорректный json");
            return;
        }

        try{
            userService.changeNickname(user.getId(), nicknameDTO.nickname());
            user.setNickname(nicknameDTO.nickname());
            session.setAttribute("current_user", user);

            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(jsonMapper.writeValueAsString(Map.of("success", true)));

        } catch (EntityNotFoundException e){
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ConflictException e){
            JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }



    }

    private void getProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("current_user");
            if (user == null){
                JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_UNAUTHORIZED, "Вы не авторизованы");
                return;
            }
            ProfileDTO userProfile = new ProfileDTO(user.getId(), user.getNickname(), user.getEmail(), user.getCreatedAt());
            String json = jsonMapper.writeValueAsString(userProfile);

            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(json);
        }
    }

    private void getInfoUserByNickname(HttpServletRequest req, HttpServletResponse resp, String nickname) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()) {
            User user = userService.findUserByNickname(nickname);
            if (user == null){
                JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
                return;
            }
            UserPublicInfoDTO userInfo = new UserPublicInfoDTO(user.getId(), user.getNickname(), user.getCreatedAt());

            String json = jsonMapper.writeValueAsString(userInfo);
            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(json);
        }
    }

    private void getInfoUser(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()) {
            User user = userService.findById(id);
            if (user == null){
                JsonUtil.sendJsonError(resp, respWriter, HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
                return;
            }
            UserPublicInfoDTO userInfo = new UserPublicInfoDTO(user.getId(), user.getNickname(), user.getCreatedAt());

            String json = jsonMapper.writeValueAsString(userInfo);
            resp.setStatus(HttpServletResponse.SC_OK);
            respWriter.println(json);
        }
    }

}
