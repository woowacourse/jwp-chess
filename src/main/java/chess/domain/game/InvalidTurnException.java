package chess.domain.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Turn For The Player")
public class InvalidTurnException extends RuntimeException {
    private static final String INVALID_TURN_ERROR = "%s의 차례입니다.";

    public InvalidTurnException(String turn) {
        super(String.format(INVALID_TURN_ERROR, turn));
    }
}
