package chess.exception;

public class InvalidAccessException extends RuntimeException {

    private InvalidAccessException(String message) {
        super(message);
    }

    public InvalidAccessException(InvalidStatus status) {
        this(status.getMessage());
    }
}
