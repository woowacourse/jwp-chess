package chess.controller;

import chess.exception.IllegalGameProgressException;
import chess.exception.IllegalMoveException;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalMoveException.class)
    public String handleMove(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addAttribute("error", "잘못된 이동입니다.");

        String referer = request.getHeader("Referer");
        String previousPath = URI.create(referer).getPath();

        return "redirect:" + previousPath;
    }

    @ExceptionHandler(IllegalGameProgressException.class)
    public String handle(RedirectAttributes redirectAttributes, IllegalGameProgressException e) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:/";
    }
}
