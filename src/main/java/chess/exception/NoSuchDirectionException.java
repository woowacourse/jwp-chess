package chess.exception;

import org.springframework.http.HttpStatus;

public class NoSuchDirectionException extends HandledException {
    public NoSuchDirectionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
