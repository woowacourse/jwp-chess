package chess.controller;

import chess.exception.ChessException;
import chess.exception.NotExistRoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ChessExceptionAdvice {


    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleChessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotExistRoomException.class)
    public ResponseEntity<String> handleNotExistRoomException(NotExistRoomException e) {
        return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
    }
}
