package chess.controller.web;

import chess.dto.ErrorMessageDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> exceptionHandle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessageDto> dataBaseExceptionHandle(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString()));
    }
}