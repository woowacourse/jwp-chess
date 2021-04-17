package chess.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentException(IllegalArgumentException error) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity emptyResultDataAccessException(EmptyResultDataAccessException error) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@");
        return ResponseEntity.badRequest().build();
    }
}
