package chess.controller;

import chess.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDto> handleException(Exception e) {
        MessageDto messageDto = new MessageDto(e.getMessage());
        return ResponseEntity.badRequest().body(messageDto);
    }
}
