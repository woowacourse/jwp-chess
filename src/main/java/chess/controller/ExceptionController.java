package chess.controller;

import chess.exception.RoomNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity roomNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity bindingFail(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return ResponseEntity.badRequest().body(bindingResult);
    }
}
