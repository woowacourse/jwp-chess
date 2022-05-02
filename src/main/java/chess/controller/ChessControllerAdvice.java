package chess.controller;

import chess.dto.ExceptionDto;
import chess.exception.NotMatchedPasswordException;
import chess.exception.NotMovableException;
import chess.exception.PieceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handle(Exception e) {
        return ResponseEntity.internalServerError().body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler({NotMovableException.class, PieceNotFoundException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionDto> handleBadRequest(Exception e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(NotMatchedPasswordException.class)
    public ResponseEntity<ExceptionDto> handleUnAuthorized(Exception e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
