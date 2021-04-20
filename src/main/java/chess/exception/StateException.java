package chess.exception;

public abstract class StateException extends RuntimeException {

    public StateException(final String message) {
        super(message);
    }
}
