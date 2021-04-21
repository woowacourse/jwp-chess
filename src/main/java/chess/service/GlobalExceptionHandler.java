package chess.service;

import chess.exception.BlankException;
import chess.exception.ChessException;
import chess.exception.MovementException;
import chess.exception.StateException;
import chess.service.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ChessException.class, MovementException.class,
        BlankException.class, StateException.class})
    public ResponseEntity<ErrorResponseDto> handleCustomException(final RuntimeException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleException(final RuntimeException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDto("서버 내부 에러입니다. "));
    }

}
