package chess.Exceptions;

public class NoRoomException extends IllegalArgumentException {

    public NoRoomException() {
        super();
    }

    public NoRoomException(String s) {
        super(s);
    }

    public NoRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRoomException(Throwable cause) {
        super(cause);
    }
}
