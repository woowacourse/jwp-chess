package chess.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class MyControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgument() {
        return "index";
    }

    @ExceptionHandler(SQLException.class)
    public String sqlException(){
        return "index";
    }
}
