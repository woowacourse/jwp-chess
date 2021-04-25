package chess.controller;

import chess.exception.ChessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessExceptionAdvice {

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleChessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultException(EmptyResultDataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("중복된 이름의 방이 있습니다.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataExcessException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB access 오류!.");
    }
}
