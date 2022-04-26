package chess.exception;

import org.springframework.dao.DataAccessException;

public class ExecuteQueryException extends DataAccessException {

    public ExecuteQueryException(String message) {
        super(message);
    }
}
