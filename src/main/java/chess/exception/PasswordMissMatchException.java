package chess.exception;

public class PasswordMissMatchException extends RuntimeException {
    public PasswordMissMatchException(String message) {
        super(message);
    }

    public PasswordMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
