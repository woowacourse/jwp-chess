package chess.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class NotFoundException extends EmptyResultDataAccessException {

    public NotFoundException(final int expectedSize) {
        super(expectedSize);
    }
}
