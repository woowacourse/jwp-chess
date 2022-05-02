package chess.controller;

import chess.dto.response.ErrorDto;
import chess.exception.WinnerIsNotExisting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler({WinnerIsNotExisting.class})
    public ResponseEntity<Void> handleWinnerIsNotExisting() {
        return ResponseEntity.noContent().build();
    }
}
