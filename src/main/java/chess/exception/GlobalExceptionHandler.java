package chess.exception;

import chess.exception.dto.ErrorMessageDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalRequestDataException.class)
    public ResponseEntity<ErrorMessageDto> IllegalRequestDataException(IllegalRequestDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(NoSuchDbDataException.class)
    public ResponseEntity<ErrorMessageDto> noSuchElementInDB(NoSuchDbDataException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessageDto> dataAccessException(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessageDto("DB 관련 작업이 실패했습니다."));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageDto> noSuchElementExceptionWhileRunning(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDto("서비스 실행도중 올바른 값을 찾지 못하는 오류가 발생했습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> otherException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageDto("서비스 실행도중 예상치 못한 오류가 발생했습니다."));
    }
}
