package chess.controller;

import chess.exception.AuthorizationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {

    private static final String USER = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute(USER);
        if (Objects.isNull(name)) {
            throw new AuthorizationException();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
