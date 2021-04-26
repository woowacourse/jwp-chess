package chess.exception.room;

import chess.exception.WebException;
import org.springframework.http.HttpStatus;

public class RoomException extends WebException {

    public RoomException(HttpStatus status, Object body) {
        super(status, body);
    }
}
