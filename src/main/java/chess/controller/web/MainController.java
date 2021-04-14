package chess.controller.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MainController {

    public MainController() { }

    @ExceptionHandler({Exception.class})
    public String handlingException(Exception e, Model model) {
        model.addAttribute("msg", e.getMessage());
        return "errorMessage";
    }
}
