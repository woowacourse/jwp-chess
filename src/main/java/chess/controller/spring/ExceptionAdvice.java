package chess.controller.spring;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<String> clientError(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
