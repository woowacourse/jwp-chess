package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
