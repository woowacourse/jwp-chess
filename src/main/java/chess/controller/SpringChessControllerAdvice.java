package chess.controller;

import chess.domain.dto.ExceptionResponseDto;
import chess.domain.exception.DataException;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler(DataException.class)
    public ResponseEntity<ExceptionResponseDto> dataExceptionResponse(DataException dataException) {
        String message = dataException.getMessage();
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(message);
        return ResponseEntity.badRequest()
            .body(exceptionResponseDto);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponseDto>  sqlExceptionResponse(SQLException sqlException) {
        String message = sqlException.getMessage();
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(message);
        return ResponseEntity.badRequest()
            .body(exceptionResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto>  illegalArgumentExceptionResponse(IllegalArgumentException
        illegalArgumentException) {
        String message = illegalArgumentException.getMessage();
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(message);
        return ResponseEntity.badRequest()
            .body(exceptionResponseDto);
    }

}
