package chess.exception;

import chess.exception.dto.ErrorMessageDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessageDto> dataAccessException(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> chessGameException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessageDto> applicationException(ApplicationException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> otherException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageDto("서버에서 처리할 수 없는 요청입니다."));
    }
}
