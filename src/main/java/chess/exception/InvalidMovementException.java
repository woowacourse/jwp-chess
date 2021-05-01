package chess.exception;

public class InvalidMovementException extends IllegalArgumentException {
    public InvalidMovementException() {
        this("부적절한 이동입니다.");
    }

    public InvalidMovementException(final String msg) {
        super(msg);
    }
}
