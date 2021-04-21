package chess.exception;

import org.springframework.http.HttpStatus;

public class NullObjectSelectionException extends HandledException {
    public NullObjectSelectionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
