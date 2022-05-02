package chess.web.controller;

import chess.web.service.dto.ErrorResponseDto;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.apache.catalina.connector.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ChessApiController.class)
public class ChessApiControllerAdvice {

    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponseDto> SQLException(SQLException exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto(Response.SC_INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<ErrorResponseDto> badRequestException(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(Response.SC_BAD_REQUEST, exception.getMessage()));
    }
}
