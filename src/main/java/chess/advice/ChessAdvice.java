package chess.advice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentExceptionHandler() {
        ExceptionState exceptionState = ExceptionState.ILLEGAL_ARGUMENT;
        return ResponseEntity.status(exceptionState.getStatus()).body(exceptionState.getErrorMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullPointerExceptionHandler() {
        ExceptionState exceptionState = ExceptionState.NULL_POINTER;
        return ResponseEntity.status(exceptionState.getStatus()).body(exceptionState.getErrorMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity emptyResultDataAccessExceptionHandler() {
        ExceptionState exceptionState = ExceptionState.EMPTY_RESULT_DATA_ACCESS;
        return ResponseEntity.status(exceptionState.getStatus()).body(exceptionState.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler() {
        ExceptionState exceptionState = ExceptionState.EXCEPTION;
        return ResponseEntity.status(exceptionState.getStatus()).body(exceptionState.getErrorMessage());
    }
}
