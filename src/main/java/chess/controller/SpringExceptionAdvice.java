package chess.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringExceptionAdvice {
    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        UnsupportedOperationException.class
    })
    public ResponseEntity<Map<String, String>> handle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(Map.of(
            "exception", exception.getMessage()
        ));
    }
}
