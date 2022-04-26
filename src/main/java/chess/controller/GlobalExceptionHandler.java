package chess.controller;

import chess.dto.response.ErrorResponseDto;
import chess.exception.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponseDto> handle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFound(final NotFoundException e) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
