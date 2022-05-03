package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleExceptions(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex){
        StringBuilder stringBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors()
            .forEach(c -> stringBuilder.append(((FieldError) c).getField())
                .append(": ")
                .append(c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(stringBuilder.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeExceptions() {
        return ResponseEntity.internalServerError().body("서버 내부 에러입니다.");
    }
}
