package chess.advice;

import chess.dto.ErrorResponseBody;
import chess.exception.HandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerAdvice {
    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponseBody> handleException(HandledException e) {
        return new ResponseEntity<>(new ErrorResponseBody(e.getMessage()), e.getHttpStatus());
    }
}
