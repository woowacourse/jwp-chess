package chess.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handle(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
