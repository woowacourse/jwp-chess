package chess.controller;

import chess.exception.ChessGameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler(ChessGameException.class)
    public ResponseEntity<String> dbException(ChessGameException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
