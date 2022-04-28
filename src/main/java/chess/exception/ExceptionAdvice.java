package chess.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<String> getException(final Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage() + "\n뒤로 가시면 됩니당.");
    }
}
