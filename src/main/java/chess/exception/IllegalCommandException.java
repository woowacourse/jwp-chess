package chess.exception;

public class IllegalCommandException extends RuntimeException {

    public IllegalCommandException(final String message) {
        super(message);
    }
}
