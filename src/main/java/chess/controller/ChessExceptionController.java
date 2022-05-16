package chess.controller;

import chess.dto.exception.ErrorMessageResponse;
import chess.exception.ClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessExceptionController {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorMessageResponse> handleClientException(ClientException error) {
        return ResponseEntity.badRequest().body(new ErrorMessageResponse(error.getMessage()));
    }
}
