package chess.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> unpredictableException(Exception error) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Void> emptyResultDataAccessException(EmptyResultDataAccessException error) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> titleNotValidException(MethodArgumentNotValidException error) {
        return ResponseEntity.badRequest().body(error.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
