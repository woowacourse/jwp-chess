package chess.domain.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "position out of bounds")
public class NotMovableDirectionException extends RuntimeException {
    private static final String NOT_MOVABLE_DIRECTION_ERROR = "이동할 수 없는 방향입니다.";

    public NotMovableDirectionException() {
        super(NOT_MOVABLE_DIRECTION_ERROR);
    }
}
