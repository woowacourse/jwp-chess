package chess.controller;

import chess.exception.IllegalGameProgressException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleMove(RedirectAttributes redirectAttributes, IllegalArgumentException e, HttpServletRequest request) {
        redirectAttributes.addAttribute("error", e.getMessage());

        String referer = request.getHeader("Referer");
        String[] split = referer.split("\\?");

        return "redirect:" + split[0];
    }

    @ExceptionHandler(IllegalGameProgressException.class)
    public String handle(RedirectAttributes redirectAttributes, IllegalGameProgressException e) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:/";
    }
}
