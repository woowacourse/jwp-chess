package chess.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    private String handelException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
