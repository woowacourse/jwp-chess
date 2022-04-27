package chess.controller;

import chess.exception.ExistGameException;
import chess.exception.IllegalDeleteException;
import chess.exception.IllegalPasswordException;
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

    @ExceptionHandler(IllegalPasswordException.class)
    public String handleEmptyResult(RedirectAttributes redirectAttributes, IllegalPasswordException e) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:/";
    }

    @ExceptionHandler(IllegalDeleteException.class)
    public String handleDelete(RedirectAttributes redirectAttributes, IllegalDeleteException e) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:/";
    }

    @ExceptionHandler(ExistGameException.class)
    public String handleExistGame(RedirectAttributes redirectAttributes, ExistGameException e) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:/";
    }
}
