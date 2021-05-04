package chess.exceptions;

public class DuplicateRoomException extends IllegalArgumentException {

    public DuplicateRoomException() {
        super();
    }

    public DuplicateRoomException(String s) {
        super(s);
    }

    public DuplicateRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRoomException(Throwable cause) {
        super(cause);
    }
}
