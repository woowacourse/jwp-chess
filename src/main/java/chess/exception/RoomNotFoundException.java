package chess.exception;

import org.springframework.dao.DataAccessException;

public class RoomNotFoundException extends DataAccessException {

    public RoomNotFoundException(final String msg) {
        super(msg);
    }
}
