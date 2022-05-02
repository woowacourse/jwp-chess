package chess.controller;

import chess.dto.response.ErrorDto;
import chess.exception.IncorrectPasswordException;
import chess.exception.MovePieceFailedException;
import chess.exception.NotFoundRoomException;
import chess.exception.WinnerIsNotExisting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    public static final int HTTP_STATUS_UNAUTHORIZED = 401;

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorDto> handleIncorrectPassword(IncorrectPasswordException e) {
        return ResponseEntity.status(HTTP_STATUS_UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(NotFoundRoomException.class)
    public ResponseEntity<Void> handleNotFoundRoom() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(WinnerIsNotExisting.class)
    public ResponseEntity<Void> handleWinnerIsNotExisting() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(MovePieceFailedException.class)
    public ResponseEntity<ErrorDto> handleMovePieceFailed(MovePieceFailedException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
