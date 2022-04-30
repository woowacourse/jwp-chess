package chess.exception;

public class IllegalDeleteRoomException extends RuntimeException {

    public IllegalDeleteRoomException() {
    }

    public IllegalDeleteRoomException(String message) {
        super(message);
    }
}
