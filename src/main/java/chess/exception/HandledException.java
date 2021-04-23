package chess.exception;

import org.springframework.http.HttpStatus;

public abstract class HandledException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected HandledException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
