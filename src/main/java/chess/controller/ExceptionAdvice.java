package chess.controller;

import chess.exception.HandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<String> handleException(HandledException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.badRequest().body("예상치 못한 에러가 발생했습니다.");
    }
}
