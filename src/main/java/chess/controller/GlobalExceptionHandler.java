package chess.controller;

import chess.dto.response.ErrorResponseDto;
import chess.exception.NotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFound(final NotFoundException e) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ErrorResponseDto> handleSqlException(final DataAccessException e,
                                                               final HttpServletRequest request) {
        logger.error(createLogMessage(request), e);
        return ResponseEntity.badRequest().body(new ErrorResponseDto("요청을 다시 확인해주세요."));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(final Exception e,
                                                                      final HttpServletRequest request) {
        logger.error(createLogMessage(request), e);
        return ResponseEntity.internalServerError().body(new ErrorResponseDto("예기치 못한 문제가 발생했습니다."));
    }

    private String createLogMessage(final HttpServletRequest request) {
        final String message = "[" + new LocalDateTime() + "] "
                + request.getMethod() + " "
                + request.getRequestURI();
        if (request.getQueryString() == null) {
            return message;
        }
        return message + "?" + request.getQueryString();
    }
}
