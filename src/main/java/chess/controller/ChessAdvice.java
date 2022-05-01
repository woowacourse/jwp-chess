package chess.controller;

import chess.exception.IllegalDeleteRoomException;
import chess.exception.IllegalMovePieceException;
import chess.exception.NoSuchGameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {

    @ExceptionHandler({
            NoSuchGameException.class, IllegalMovePieceException.class, IllegalDeleteRoomException.class
    })
    public ResponseEntity<String> handleNoSuchGameException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
