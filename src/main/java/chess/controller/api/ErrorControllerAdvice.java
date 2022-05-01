package chess.controller.api;

import chess.dto.ErrorRes;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRes> argumentErrorResponse(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRes> stateErrorResponse(IllegalStateException e) {
        return ResponseEntity.badRequest().body(new ErrorRes(e.getMessage()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorRes> errorDbResponse() {
        return ResponseEntity.badRequest().body(new ErrorRes("값이 존재하지 않습니다."));
    }
}
