package chess.domain.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "position out of bounds")
public class PositionOutOfBoundsException extends RuntimeException {
    private static final String OUT_OF_BOUNDS_ERROR = "존재하지 않는 위치입니다.";

    public PositionOutOfBoundsException() {
        super(OUT_OF_BOUNDS_ERROR);
    }
}
