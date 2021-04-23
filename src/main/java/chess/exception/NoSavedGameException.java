package chess.exception;

import org.springframework.http.HttpStatus;

public class NoSavedGameException extends HandledException {
    public NoSavedGameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
