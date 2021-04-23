package chess.exception;

import org.springframework.http.HttpStatus;

public class InvalidMoveStrategyException extends HandledException {
    public InvalidMoveStrategyException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
