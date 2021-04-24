package chess.controller;

import chess.domain.dto.BoardDto;
import chess.domain.dto.response.ApiResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponseDto<BoardDto> excepationHandling(Exception exception) {
        return ApiResponseDto.BAD_REQUEST(null, exception.getMessage());
    }
}
