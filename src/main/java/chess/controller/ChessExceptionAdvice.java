package chess.controller;

import chess.exception.WebException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessExceptionAdvice {

    @ExceptionHandler(WebException.class)
    public ResponseEntity handleChessException(WebException e) {
        return new ResponseEntity(e.getBody(), e.getStatus());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataExcessException(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB access 오류!");
    }
}
