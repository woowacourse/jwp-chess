package chess.controller.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ErrorAdvice {

    public ErrorAdvice() {
    }

    @ExceptionHandler(SQLException.class)
    private String SQLExceptionHandler(SQLException e, Model model) {
        model.addAttribute("msg", e.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private String IllegalArgumentExceptionHandler(IllegalArgumentException e, Model model) {
        model.addAttribute("msg", e.getMessage());
        return "errorPage";
    }
}
