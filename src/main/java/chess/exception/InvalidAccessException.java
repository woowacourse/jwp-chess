package chess.exception;

import org.springframework.http.HttpStatus;

public class InvalidAccessException extends RuntimeException {

    private final HttpStatus httpStatus;

    private InvalidAccessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public InvalidAccessException(InvalidStatus status) {
        this(status.getMessage(), status.getCode());
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
