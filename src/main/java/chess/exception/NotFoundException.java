package chess.exception;

import org.springframework.dao.DataAccessException;

public class NotFoundException extends DataAccessException {

    public NotFoundException(final String msg) {
        super(msg);
    }
}
