package chess.exception;

import org.springframework.http.HttpStatus;

public class InvalidTurnException extends HandledException {
    public InvalidTurnException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
