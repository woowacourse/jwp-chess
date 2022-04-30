package chess.controller;

import chess.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException() {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto("서버에 문제가 발생했습니다. 다시 시도해주세요.");
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
