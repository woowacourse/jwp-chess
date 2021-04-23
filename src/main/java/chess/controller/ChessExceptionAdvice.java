package chess.controller;

import chess.exception.ChessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ChessExceptionAdvice {

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleChessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
