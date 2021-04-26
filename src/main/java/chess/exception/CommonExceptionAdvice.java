package chess.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> dataNotFoundHandle(final Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> badRequestHandle(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
