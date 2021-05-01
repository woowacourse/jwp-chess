package chess.exception;

import chess.domain.exception.CannotCreateGameException;
import chess.dto.CreateExceptionResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handle(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CannotCreateGameException.class)
    public ResponseEntity<CreateExceptionResponseDto> gameCreateHandle(final Exception e) {
        return ResponseEntity.badRequest().body(new CreateExceptionResponseDto(e.getMessage()));
    }
}
