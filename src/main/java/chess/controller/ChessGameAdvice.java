package chess.controller;

import chess.exception.ChessGameException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

@ControllerAdvice
public class ChessGameAdvice {

    @ExceptionHandler(ChessGameException.class)
    @ResponseBody
    public ResponseEntity<String> handle(ChessGameException chessGameException) {
        return new ResponseEntity<>(
                chessGameException.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    @ResponseBody
    public ResponseEntity<String> handleSQLException(Exception e){
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(NoHandlerFoundException e, Model model){
        model.addAttribute("exception", e);
        return "/error/error_page";
    }

    @ExceptionHandler(Exception.class)
    public String commonExcept(Exception e, Model model) {
        model.addAttribute("exception", e);
        return "/error/error_page";
    }
}
