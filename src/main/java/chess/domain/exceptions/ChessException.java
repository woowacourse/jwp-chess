package chess.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ChessException extends RuntimeException {

    HttpStatus status;
    String message;

//    public ChessException(String message) {
//        this.message = message;
//    }

    public ChessException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
