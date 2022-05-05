package chess.controller.handler;

import chess.exception.WrongPasswordException;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPassword() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        Gson gson = new Gson();
        return ResponseEntity.badRequest().body(gson.toJson(exception.getMessage()));
    }
}
