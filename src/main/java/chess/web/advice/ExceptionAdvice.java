package chess.web.advice;

import chess.service.dto.response.ExceptionResponse;
import chess.web.controller.ChessController;
import chess.web.controller.GameApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {GameApiController.class, ChessController.class})
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleError(Exception ex) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(ex.getMessage()));
    }
}
