package chess.web.advice;

import chess.service.dto.response.ExceptionResponse;
import chess.web.controller.ChessController;
import chess.web.controller.GameApiController;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {GameApiController.class, ChessController.class})
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleError(Exception ex) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDatabaseError() {
        return ResponseEntity.badRequest().body(new ExceptionResponse("데이터 관련 문제가 생겼습니다!"));
    }
}
