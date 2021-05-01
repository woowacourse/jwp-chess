package chess.web.apicontroller;

import chess.domain.user.WrongPasswordException;
import chess.exception.AuthenticationFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleBadRequest(final DataAccessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<String> handleBadRequest(final WrongPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
