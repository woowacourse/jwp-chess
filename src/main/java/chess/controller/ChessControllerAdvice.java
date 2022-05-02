package chess.controller;

import chess.dto.ExceptionResponse;
import chess.exception.DeleteFailOnPlayingException;
import chess.exception.PasswordNotMatchedException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {ChessController.class})
public class ChessControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unknownExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class,
            DeleteFailOnPlayingException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponse> knownExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordNotMatchedException.class})
    public ResponseEntity<ExceptionResponse> unauthorizedExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
