package chess.service;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK(HttpStatus.OK),
    MOVED_PERMANENTLY (HttpStatus.MOVED_PERMANENTLY),
    FOUND (HttpStatus.FOUND),
    SERVER_ERROR(HttpStatus.NOT_IMPLEMENTED);

    private final HttpStatus httpStatus;

    ResponseCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
