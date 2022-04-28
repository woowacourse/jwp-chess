package chess.controller.exception;

import chess.exception.ChessGameException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ChessGameExceptionHandler {

    @ExceptionHandler(ChessGameException.class)
    public String exceptionHandler(ChessGameException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/chess-game?chess-game-id=" + e.getChessGameId();
    }
}
