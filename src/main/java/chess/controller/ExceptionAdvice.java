package chess.controller;

import chess.controller.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    private static final String DEFAULT_ERROR_MESSAGE = "체스 게임을 실행할 수 없습니다. 관리자에게 문의해주세요.";

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(DEFAULT_ERROR_MESSAGE);
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
