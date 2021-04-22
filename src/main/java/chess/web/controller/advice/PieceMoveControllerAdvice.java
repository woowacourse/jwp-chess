package chess.web.controller.advice;

import chess.web.controller.ChessGamePlayController;
import chess.web.controller.dto.response.MoveResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ChessGamePlayController.class)
public class PieceMoveControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public MoveResponseDto pieceMoveException(IllegalArgumentException e) {
        return new MoveResponseDto(true, e.getMessage());
    }
}
