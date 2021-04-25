package chess.controller;

import chess.exception.ChessException;
import chess.exception.DuplicatedRoomNameException;
import chess.exception.NotExistRoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler(NotExistRoomException.class)
    public ResponseEntity<String> dbException(NotExistRoomException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> chessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedRoomNameException.class)
    public ResponseEntity<String> duplicatedRoomNameException(DuplicatedRoomNameException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
