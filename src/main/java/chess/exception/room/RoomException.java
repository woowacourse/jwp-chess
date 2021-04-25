package chess.exception.room;

import org.springframework.dao.DataAccessException;

public class RoomException extends DataAccessException {

    public RoomException(String msg) {
        super(msg);
    }
}
