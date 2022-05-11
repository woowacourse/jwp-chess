package chess.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handle(Exception exception) {
        return ResponseEntity.internalServerError()
            .body(Map.of("exception", exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
            .body(Map.of("exception", exception.getMessage()));
    }
}
