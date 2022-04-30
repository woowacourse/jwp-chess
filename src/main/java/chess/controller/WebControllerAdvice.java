package chess.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String exception(Exception exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "errorpage";
    }
}
