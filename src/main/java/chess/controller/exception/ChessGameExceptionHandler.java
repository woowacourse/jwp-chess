package chess.controller.exception;

import chess.exception.ChessGameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ChessGameExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "알 수 없는 오류입니다. 서버 관리자에게 문의해주세요.");
        log.error(e.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", "서버 오류입니다.");
        log.warn(e.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentExceptionHandler(IllegalArgumentException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        log.warn(e.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(ChessGameException.class)
    public String chessGameExceptionHandler(ChessGameException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        log.debug(e.getMessage());
        return "redirect:/chess-game?id=" + e.getChessGameId();
    }
}
