package chess.service;

import chess.service.dto.CommonResponseDto;
import chess.service.dto.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public CommonResponseDto handleRuntimeException(RuntimeException exception) {
        logger.info(exception.getMessage());
        return new CommonResponseDto<>(ResponseCode.BAD_REQUEST.code(), exception.getMessage());
    }

}
