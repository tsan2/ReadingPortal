package ru.anastasya.readingportal.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        long timeExecution = System.currentTimeMillis() - (long) request.getAttribute("startTime");
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        System.out.println("По запросу на путь " + request.getRequestURI() +
                " был выполнен метод " + handlerMethod.getMethod() +
                ". Выполнение заняло " + timeExecution + " мс");
    }
}
