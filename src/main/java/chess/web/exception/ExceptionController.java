package chess.web.exception;

import chess.web.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity handle(Exception e) {
        return ResponseEntity.badRequest().body(ErrorDto.of(e.getMessage()));
    }
}
