package chess.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

import static spark.Spark.exception;

@Controller
public class MainController {

    public MainController() {
    }

    @ExceptionHandler(SQLException.class)
    private void handlingSQLException() {
        exception(SQLException.class, (e, req, res) -> {
            res.status(404);
            res.body("SQL error : " + e.getMessage());
        });
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private void handlingIllegalArgumentException() {
        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(404);
            res.body("Unexpected error : " + e.getMessage());
        });
    }
}
