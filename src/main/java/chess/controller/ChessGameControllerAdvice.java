package chess.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ChessGameControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> IllegalArgumentExceptionHandle(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({SQLException.class, NullPointerException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<String> DBExceptionHandle(SQLException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}
