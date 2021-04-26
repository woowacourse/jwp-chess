package chess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PieceMoveExceptionAdvice {

    @ExceptionHandler(PieceMoveException.class)
    public ResponseEntity<String> pieceMoveExceptionHandle(PieceMoveException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotExistDataException.class)
    public ResponseEntity<String> notExistDataExceptionHandle(NotExistDataException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<String> dataBaseExceptionHandle(DataBaseException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
