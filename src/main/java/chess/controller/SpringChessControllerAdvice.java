package chess.controller;

import chess.domain.dto.ErrorMessageDto;
import chess.domain.exception.DataException;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler(DataException.class)
    public ResponseEntity<ErrorMessageDto> dataExceptionResponse(DataException dataException) {
        String message = dataException.getMessage();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(message);
        return ResponseEntity.badRequest()
            .body(errorMessageDto);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorMessageDto>  sqlExceptionResponse(SQLException sqlException) {
        String message = sqlException.getMessage();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(message);
        return ResponseEntity.badRequest()
            .body(errorMessageDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto>  illegalArgumentExceptionResponse(IllegalArgumentException
        illegalArgumentException) {
        String message = illegalArgumentException.getMessage();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(message);
        return ResponseEntity.badRequest()
            .body(errorMessageDto);
    }

}
