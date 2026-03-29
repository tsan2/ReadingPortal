package ru.anastasya.readingportal.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ru.anastasya.readingportal.models.User;
import ru.anastasya.readingportal.services.UserService;
import ru.anastasya.readingportal.utils.PasswordUtil;
import ru.anastasya.readingportal.utils.TokenUtil;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private final UserService userService = UserService.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();

        if (session.getAttribute("current_user") != null){
            filterChain.doFilter(req, servletResponse);
            return;
        }

        String rememberMeToken = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equalsIgnoreCase("REMEMBER_ME")){
                    rememberMeToken = cookie.getValue();
                    break;
                }
            }
        }

        if (rememberMeToken != null){
            User user = userService.findUserByTokenHash(TokenUtil.hashToken(rememberMeToken));
            if (user != null){
                session.setAttribute("current_user", user);
            }
        }

        filterChain.doFilter(req, servletResponse);

    }
}
