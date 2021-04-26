package chess.advice;

import chess.controller.web.dto.ErrorMessageResponseDto;
import chess.exception.ChessGameException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ChessGameException.class)
    public ResponseEntity<ErrorMessageResponseDto> chessGameExceptionHandle(ChessGameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessageResponseDto> dbExceptionHandle(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageResponseDto> RuntimeExceptionHandle(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
