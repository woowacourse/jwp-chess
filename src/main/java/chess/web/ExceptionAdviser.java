package chess.web;

import chess.web.dto.ErrorMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(exception.getMessage()));
    }
}
