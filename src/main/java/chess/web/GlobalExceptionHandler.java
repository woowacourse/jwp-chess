package chess.web;

import chess.exception.InvalidChessObjectException;
import chess.exception.NoWinnerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidChessObjectException.class)
    protected ResponseEntity<Object> handleInvalidRequest(InvalidChessObjectException exception,
        WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoWinnerException.class)
    protected ResponseEntity<Object> handleNoWinnerRequest(NoWinnerException exception,
        WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException exception,
        WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(),
            new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
