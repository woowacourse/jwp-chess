package chess.web.controller;

import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, UnsupportedOperationException.class})
    public ResponseEntity<String> handleGameException(Exception exception) {
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String handleSQLException(Exception exception, Model model) {
        model.addAttribute("exception", exception);
        return "/error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException exception, Model model){
        model.addAttribute("exception", exception);
        return "/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleCommonException(Exception exception, Model model) {
        model.addAttribute("exception", exception);
        return "/error";
    }
}
