package chess.controller.spring;

import chess.exception.ChessException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ChessException.class)
    public String handleIllegalArgumentException(HttpServletResponse response, ChessException e) {
        Cookie cookie = new Cookie("em", encodeCookie(e.getErrorMessage()));
        System.out.println("게임아이디" + e.getGameId());
        cookie.setPath("/games/" + e.getGameId());
        response.addCookie(cookie);
        return "redirect:/games/" + e.getGameId();
    }

    private String encodeCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        return new String(Base64.getUrlEncoder().withoutPadding()
            .encode(cookie.getBytes(StandardCharsets.UTF_8)));
    }
}