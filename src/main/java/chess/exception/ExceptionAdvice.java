package chess.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<String> getException(final Exception exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage() + "\n\n뒤로 가기 후 제대로 입력해보세용");
    }
}
