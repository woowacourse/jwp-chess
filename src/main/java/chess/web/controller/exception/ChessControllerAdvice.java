package chess.web.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
