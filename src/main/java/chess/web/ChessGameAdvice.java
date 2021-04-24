package chess.web;

import chess.exception.ChessGameException;
import chess.web.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ChessGameAdvice {
    @ExceptionHandler
    public ResponseEntity<MessageDto> handle(ChessGameException e) {
        return ResponseEntity.badRequest().body(
                new MessageDto(e.getMessage())
        );
    }
}
