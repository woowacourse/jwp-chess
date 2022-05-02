package chess.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgument() {
        return "index";
    }
}
