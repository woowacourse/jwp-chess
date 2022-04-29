package chess.web.controller;

import chess.web.service.dto.ErrorResponseDto;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessApiControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentException(IllegalArgumentException exception) {
        String message = exception.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponseDto(Response.SC_BAD_REQUEST, message));
    }
}
