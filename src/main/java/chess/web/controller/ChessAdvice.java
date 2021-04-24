package chess.web.controller;

import chess.exception.ChessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessAdvice {

    @ExceptionHandler(ChessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String chessExceptionHandler(ChessException e) {
        return e.getMessage();
    }
}
