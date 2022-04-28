package chess.exception;

import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponse> badRequestHandle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> notFoundHandle(final Exception e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle() {
        return ResponseEntity.internalServerError().build();
    }
}
