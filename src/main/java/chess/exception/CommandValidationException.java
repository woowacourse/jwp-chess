package chess.exception;

import org.springframework.http.HttpStatus;

public class CommandValidationException extends HandledException {
    public CommandValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
