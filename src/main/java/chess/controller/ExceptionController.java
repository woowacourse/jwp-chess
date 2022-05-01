package chess.controller;

import chess.dto.response.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
