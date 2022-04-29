package chess.controller;

import chess.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleIllegalArgumentException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
