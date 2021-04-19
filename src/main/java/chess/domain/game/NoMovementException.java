package chess.domain.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Move Command Size")
public class NoMovementException extends RuntimeException {
    private static final String NO_MOVEMENT_ERROR = "현재 위치와 동일한 위치로 이동할 수 없습니다.";

    public NoMovementException() {
        super(NO_MOVEMENT_ERROR);
    }
}
