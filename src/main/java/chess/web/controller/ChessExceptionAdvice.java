package chess.web.controller;

import chess.web.controller.dto.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessExceptionAdvice {

    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        DataAccessException.class
    })
    public ResponseEntity<ErrorResponse> badRequestHandle(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handle() {
        return ResponseEntity.internalServerError().build();
    }
}
