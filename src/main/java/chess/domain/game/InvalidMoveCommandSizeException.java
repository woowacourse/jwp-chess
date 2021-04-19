package chess.domain.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Move Command Size")
public class InvalidMoveCommandSizeException extends RuntimeException {
    private static final String INVALID_COMMAND_SIZE_ERROR = "move command 는 (move source target) 이어야 합니다.";

    public InvalidMoveCommandSizeException() {
        super(INVALID_COMMAND_SIZE_ERROR);
    }
}
