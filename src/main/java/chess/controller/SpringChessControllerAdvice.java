package chess.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().body("unavailable");
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<String> handleSQLException() {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("fail");
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<String> handleEmptyResultException() {
        return ResponseEntity.status(NOT_FOUND).body("not-found");
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<String> handleLoginException() {
        return ResponseEntity.status(UNAUTHORIZED).body("unauthorized");
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<String> DuplicateKeyException() {
        return ResponseEntity.status(CONFLICT).body("conflict");
    }
}
