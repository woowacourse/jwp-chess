package chess.advice;

import exception.ChessException;
import exception.ExceptionStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessAdvice {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccessException() {
        ExceptionStatus exceptionStatus = ExceptionStatus.EMPTY_RESULT_DATA_ACCESS;
        return ResponseEntity.status(exceptionStatus.getHttpStatus()).body(exceptionStatus.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e) {
        System.out.println(e.getMessage());
        ExceptionStatus exceptionStatus = ExceptionStatus.EXCEPTION;
        return ResponseEntity.status(exceptionStatus.getHttpStatus()).body(exceptionStatus.getMessage());
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handleChessException(final ChessException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
