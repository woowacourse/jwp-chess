package chess.controller;

import chess.controller.dto.response.ChessGameErrorResponse;
import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessGameControllerAdvice {

    private static final String INTERNAL_EXCEPTION_MESSAGE = "서버 내부 에러입니다.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ChessGameErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach((error) -> stringBuilder.append(error.getDefaultMessage())
                .append(System.lineSeparator()));
        return ResponseEntity.badRequest().body(ChessGameErrorResponse.create(stringBuilder.toString()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<ChessGameErrorResponse> handleBusinessException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(ChessGameErrorResponse.from(runtimeException));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Void> handleSqlException() {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ChessGameErrorResponse> handleException() {
        return ResponseEntity.internalServerError().body(ChessGameErrorResponse.create(INTERNAL_EXCEPTION_MESSAGE));
    }
}
