package chess.exception;

public abstract class ChessException extends RuntimeException {

    public ChessException(final String message) {
        super(message);
    }
}
