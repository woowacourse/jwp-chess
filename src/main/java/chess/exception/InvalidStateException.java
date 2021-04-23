package chess.exception;

import org.springframework.http.HttpStatus;

public class InvalidStateException extends HandledException {
    public InvalidStateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
