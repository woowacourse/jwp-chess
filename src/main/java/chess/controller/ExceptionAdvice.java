package chess.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice("chess.controller")
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NoSuchElementException.class, UnsupportedOperationException.class})
    public ResponseEntity<ErrorMessageResponseDto> badRequestErrorHandle(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorMessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<ErrorMessageResponseDto> unauthorizedErrorHandle(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
