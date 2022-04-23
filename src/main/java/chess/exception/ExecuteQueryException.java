package chess.exception;

import org.springframework.http.HttpStatus;

public class ExecuteQueryException extends ApplicationException {

    public ExecuteQueryException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
    }

}
