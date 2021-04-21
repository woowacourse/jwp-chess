package chess.controller.web;

import chess.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> exceptionHandle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }
}
