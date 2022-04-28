package chess.exception;

public class NotExistRoomException extends RuntimeException {

    public NotExistRoomException(final String message) {
        super(message);
    }
}
