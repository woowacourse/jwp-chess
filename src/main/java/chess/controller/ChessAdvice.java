package chess.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleSQLException() {
        return ResponseEntity.badRequest().body("SQL 에러 발생");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleDomainException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
