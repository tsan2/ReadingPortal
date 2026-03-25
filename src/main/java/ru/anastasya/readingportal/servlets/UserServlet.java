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
import ru.anastasya.readingportal.dto.ProfileDTO;
import ru.anastasya.readingportal.dto.UserPublicInfoDTO;
import ru.anastasya.readingportal.dto.UserSummaryDTO;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.equals("/me")){
            getProfile(req, resp);
        }
        else if (pathInfo.isBlank() || pathInfo.equals("/")){
            getAllUser(req, resp);
        }

        try{
            String num = pathInfo.replace("/", "");
            Long id = Long.parseLong(num);
            getInfoUser(req, resp, id);
        } catch (NumberFormatException e) {
            resp.setContentType("application/json; charset=UTF-8");
            sendJsonError(resp, resp.getWriter(), HttpServletResponse.SC_BAD_REQUEST, "Введён некорректный id пользователя");
        }


    }

    private void getAllUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Самое начало");
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()){
            int page = Integer.parseInt(req.getParameter("page"));
            int size = Integer.parseInt(req.getParameter("size"));

            System.out.println("Начинаю вызов");
            List<UserSummaryDTO> users = userService.findAllUser(page, size);
            System.out.println("Вызов закончен");
            Map<String, List<UserSummaryDTO>> userMap = new HashMap<>();
            userMap.put("users", users);
            String json = jsonMapper.writeValueAsString(userMap);
            respWriter.println(json);
        }

    }

    private void getProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("current_user");
            if (user == null){
                sendJsonError(resp, respWriter, HttpServletResponse.SC_UNAUTHORIZED, "Вы не авторизованы");
                return;
            }
            ProfileDTO userProfile = new ProfileDTO(user.getId(), user.getNickname(), user.getEmail(), user.getCreatedAt());
            String json = jsonMapper.writeValueAsString(userProfile);

            respWriter.println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void getInfoUser(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try(PrintWriter respWriter = resp.getWriter()) {
            User user = userService.findById(id);
            if (user == null){
                sendJsonError(resp, respWriter, HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
                return;
            }
            UserPublicInfoDTO userInfo = new UserPublicInfoDTO(user.getId(), user.getNickname(), user.getEmail(), user.getCreatedAt());

            String json = jsonMapper.writeValueAsString(userInfo);
            respWriter.println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void sendJsonError(HttpServletResponse resp, PrintWriter responseWriter, int status, String message) throws IOException {
        resp.setStatus(status);
        jsonMapper.writeValue(responseWriter, Map.of("error", message));

    }
}
