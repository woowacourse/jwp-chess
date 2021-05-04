package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> checkChessGameException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<String> checkCookieMissing(Exception exception) {
        return ResponseEntity.badRequest().body("로그인을 해 주세요.");
    }
}
