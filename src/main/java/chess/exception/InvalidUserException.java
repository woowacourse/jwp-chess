package chess.exception;

public class InvalidUserException extends IllegalArgumentException {
    public InvalidUserException() {
        this("적절하지 않은 사용자입니다.");
    }

    public InvalidUserException(final String msg) {
        super(msg);
    }
}
