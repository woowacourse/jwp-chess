package chess.controller;

import chess.dto.ErrorResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponseDto> handle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
