package chess.controller.web.advice;

import chess.controller.web.dto.exception.ExceptionMessageResponseDto;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionMessageResponseDto> internalServerErrorByDataAccessException(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(JdbcSQLSyntaxErrorException.class)
    public ResponseEntity<ExceptionMessageResponseDto> jdbcSQLSyntaxErrorException(JdbcSQLSyntaxErrorException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionMessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMessageResponseDto> badRequest(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessageResponseDto(e.getMessage()));
    }
}
