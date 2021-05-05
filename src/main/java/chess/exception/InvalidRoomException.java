package chess.exception;

public class InvalidRoomException extends IllegalArgumentException {
    public InvalidRoomException() {
        this("유효하지 않은 방입니다.");
    }

    public InvalidRoomException(final String msg) {
        super(msg);
    }
}
