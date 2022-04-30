package chess.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessGameRestControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Void> badArgumentHandle(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Void> badStateHandle(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
