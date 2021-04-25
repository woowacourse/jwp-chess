package chess.controller;

import chess.exception.chessgame.ChessException;
import chess.exception.room.RoomException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessExceptionAdvice {

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleChessException(ChessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<Object> handleNotExistRoomException(RoomException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataExcessException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB access 오류!.");
    }
}
