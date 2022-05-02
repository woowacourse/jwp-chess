package chess.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class RoomNotFoundException extends EmptyResultDataAccessException {

    public RoomNotFoundException(final int expectedSize) {
        super(expectedSize);
    }
}
