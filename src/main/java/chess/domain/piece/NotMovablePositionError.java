package chess.domain.piece;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot Move Piece to such Position")
public class NotMovablePositionError extends RuntimeException {
    private static final String NOT_MOVABLE_POSITION_ERROR = "이동할 수 없는 위치입니다.";

    public NotMovablePositionError() {
        super(NOT_MOVABLE_POSITION_ERROR);
    }
}
