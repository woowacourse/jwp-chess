package chess.exception;

public abstract class MovementException extends RuntimeException {

    public MovementException(final String message) {
        super(message);
    }
}
