package chess.web.controller;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessWebControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public String hasNotChessRoom(NoSuchElementException exception) {
        return "noChess";
    }
}
