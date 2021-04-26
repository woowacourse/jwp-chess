package chess.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import chess.controller.SpringChessGameRestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(SpringChessGameRestController.class);

    @ExceptionHandler(DriverLoadException.class)
    private ResponseEntity driverLoadExceptionHandle(DriverLoadException e) {
        LOGGER.error(e.getStackTrace());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("!! JDBC Driver load 오류");
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    private ResponseEntity dataAccessExceptionHandle(DataAccessException e) {
        LOGGER.error(e.getStackTrace());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("!! Database Access 오류");
    }
}
