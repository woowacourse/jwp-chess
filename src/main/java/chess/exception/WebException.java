package chess.exception;

import org.springframework.http.HttpStatus;

public class WebException extends RuntimeException {
    private final HttpStatus status;
    private final Object body;

    public WebException(Object body) {
        this.status = HttpStatus.BAD_REQUEST;
        this.body = body;
    }

    public WebException(HttpStatus status, Object body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Object getBody() {
        return body;
    }
}
