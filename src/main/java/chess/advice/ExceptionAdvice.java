package chess.advice;

import chess.controller.web.dto.ErrorMessageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageResponseDto> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
