package chess.controller.exception;

import chess.exception.NotFoundChessGamePageException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundChessGamePageException.class)
    public String handleNotFoundChessGame(NotFoundChessGamePageException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error-page";
    }

}
