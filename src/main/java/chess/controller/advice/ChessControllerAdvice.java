package chess.controller.advice;

import chess.dto.ErrorResponse;
import chess.exception.HandledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessControllerAdvice {
    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(HandledException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("예상치 못한 서버 에러가 발생했습니다.\n에러 메세지 : " + e.getMessage()));
    }
}
