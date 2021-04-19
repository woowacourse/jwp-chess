package chess.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PieceMoveExceptionAdvice {

    @ExceptionHandler(PieceMoveException.class)
    public ResponseEntity<String> exceptionHandle(PieceMoveException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
