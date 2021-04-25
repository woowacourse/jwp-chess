package chess.controller.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorAdvice {

    public ErrorAdvice() {
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity IllegalArgumentExceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
