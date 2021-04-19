package chess.exception;

import chess.dto.response.MoveResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessAdvice {
    @ExceptionHandler(RuntimeException.class)
    public MoveResponseDto move(final RuntimeException runtimeException) {
        return new MoveResponseDto(false, runtimeException.getMessage());
    }
}
