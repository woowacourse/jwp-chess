package chess.application.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RedirectAttributes redirectAttributes, RuntimeException e) {
        redirectAttributes.addAttribute("message", e.getMessage());
        return "redirect:";
    }
}
