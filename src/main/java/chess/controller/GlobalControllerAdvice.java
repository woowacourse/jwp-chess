package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledException(final Exception e) {
        final String message = "Unhandled exception : " + e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final Exception e) {
        final String message = "IllegalArgumentException exception : " + e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(final Exception e) {
        final String message = "SQL exception : " + e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
