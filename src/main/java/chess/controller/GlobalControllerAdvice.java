package chess.controller;

import chess.exception.InvalidMovementException;
import chess.exception.InvalidRoomException;
import chess.exception.InvalidUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequest(final BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final FieldError mainError = fieldErrors.get(0);

        final String message = "Bad request Exception : " + mainError.getDefaultMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({
            InvalidMovementException.class,
            InvalidRoomException.class,
            InvalidUserException.class
    })
    public ResponseEntity handleBadAccess(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException() {
        return ResponseEntity.badRequest().body("Unhandled Exception");
    }
}
