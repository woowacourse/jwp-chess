package chess.service;

import chess.service.dto.CommonResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new CommonResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

}
