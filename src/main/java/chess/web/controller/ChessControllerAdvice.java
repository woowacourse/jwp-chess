package chess.web.controller;

import chess.exception.ChessGameException;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler(ChessGameException.class)
    public ResponseEntity<String> handleGameException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<String> handleSQLException() {
        return new ResponseEntity<>("잘못된 DB 접근입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoHandlerFoundException() {
        return new ResponseEntity<>("잘못된 주소입니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleCommonException() {
        return new ResponseEntity<>("잘못된 접근입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
