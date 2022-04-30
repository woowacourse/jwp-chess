package chess.web.controller;

import java.util.NoSuchElementException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = ChessWebController.class)
public class ChessWebControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public String hasNotChessRoom(NoSuchElementException exception) {
        return "noChess";
    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
    }
}
