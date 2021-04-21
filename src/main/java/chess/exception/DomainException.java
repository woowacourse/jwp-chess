package chess.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends HandledException {
    public DomainException(String message) {
        super(message, HttpStatus.NOT_ACCEPTABLE);
    }
}
