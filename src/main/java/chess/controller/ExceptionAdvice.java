package chess.controller;

import chess.controller.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
