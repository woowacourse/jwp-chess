package chess.controller.web;

import chess.controller.web.dto.ErrorMessageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice("chess.controller.web")
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NoSuchElementException.class, UnsupportedOperationException.class})
    public ResponseEntity<ErrorMessageResponseDto> handle(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
