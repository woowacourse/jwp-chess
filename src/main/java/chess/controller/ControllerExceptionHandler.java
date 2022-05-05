package chess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import chess.exception.DeleteChessGameException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({DeleteChessGameException.class})
    public String handleDeleteException(RedirectAttributes redirectAttributes,
        DeleteChessGameException exception) {
        logger.error(exception.getMessage());
        redirectAttributes.addAttribute("error", exception.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler({DataAccessException.class})
    public String handleDataAccessException(DataAccessException exception) {
        logger.error(exception.getMessage());
        return "error";
    }
}
