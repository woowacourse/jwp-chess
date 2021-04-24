package chess.controller.chess;

import chess.controller.ApiError;
import chess.exception.BusinessException;
import chess.exception.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> businessExceptionHandler(BusinessException e) {
        return ResponseEntity.badRequest().body(ApiError.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> dbExceptionHandler(DataAccessException e) {
        return ResponseEntity.badRequest().body(ApiError.of(ErrorCode.DB_COMMON, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> etcExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(ApiError.of(ErrorCode.ETC, e.getMessage()));
    }
}
