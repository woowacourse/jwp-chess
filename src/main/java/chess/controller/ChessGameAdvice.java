package chess.controller;

import chess.exception.ChessGameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessGameAdvice {

    @ExceptionHandler(ChessGameException.class)
    public ResponseEntity<String> handle(ChessGameException chessGameException) {
        return ResponseEntity.badRequest().body(chessGameException.getMessage());
    }
}
