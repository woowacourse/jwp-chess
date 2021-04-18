package chess.domain.piece;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unable To Move Blank")
public class BlankMoveException extends RuntimeException {
    private static final String BLANK_MOVE_ERROR = "공백은 움직일 수 없습니다.";

    public BlankMoveException() {
        super(BLANK_MOVE_ERROR);
    }
}
