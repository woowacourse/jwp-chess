package chess.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessGameRestControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Void> badArgumentHandle(final Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
