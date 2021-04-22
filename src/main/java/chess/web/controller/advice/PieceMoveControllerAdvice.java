package chess.web.controller.advice;

import chess.web.controller.ChessGamePlayController;
import chess.web.controller.dto.response.MoveErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ChessGamePlayController.class)
public class PieceMoveControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MoveErrorResponseDto> pieceMoveException(IllegalArgumentException e) {
        return new ResponseEntity<>(new MoveErrorResponseDto(e), HttpStatus.OK);
    }
}
