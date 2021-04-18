package chess.domain.exception;

import org.springframework.dao.DataAccessException;

public class DataException extends DataAccessException {
    public DataException(String msg) {
        super(msg);
    }
}
