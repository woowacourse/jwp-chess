package chess.exception;

public abstract class HandledException extends RuntimeException {
    public HandledException(String message) {
        super(message);
    }
}
