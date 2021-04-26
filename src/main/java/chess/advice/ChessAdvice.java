package chess.advice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, EmptyResultDataAccessException.class})
    public ResponseEntity IllegalArgumentExceptionHandler() {
        return ResponseEntity.badRequest().build();
    }
}
