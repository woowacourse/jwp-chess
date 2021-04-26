package chess.controller;

import chess.exception.ChessException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleSQLException() {
        return new ResponseEntity<>("SQL 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleDomainException(ChessException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getErrorMessage());
    }
}
