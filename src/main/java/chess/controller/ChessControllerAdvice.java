package chess.controller;

import chess.dto.response.ErrorResponseDto;
import chess.exception.IllegalCommandException;
import chess.exception.NotExistRoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler({IllegalCommandException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest(final Exception e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler({NotExistRoomException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFound() {
        return ResponseEntity.notFound()
                .build();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException() {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto("오류가 발생했습니다. 관리자에게 문의해주세요."));
    }
}
