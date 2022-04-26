package chess.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(RedirectAttributes redirectAttributes, IllegalArgumentException e, HttpServletRequest request) {
        redirectAttributes.addAttribute("error", e.getMessage());

        return "redirect:" + request.getHeader("Referer");
    }
}
