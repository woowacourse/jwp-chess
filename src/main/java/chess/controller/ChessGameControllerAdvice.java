package chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ChessGameControllerAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> runTimeExceptionHandle(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> IllegalArgumentExceptionHandle(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<String> DBExceptionHandle(SQLException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}
