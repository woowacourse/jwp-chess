package chess.exception;

public class InvalidPasswordException extends IllegalArgumentException {

    private static final String INVALID_PASSWORD_MESSAGE = "Password가 일치하지 않습니다.";

    public InvalidPasswordException() {
        this(INVALID_PASSWORD_MESSAGE, new IllegalArgumentException());
    }

    private InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
