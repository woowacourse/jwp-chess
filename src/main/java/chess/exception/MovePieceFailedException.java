package chess.exception;

public class MovePieceFailedException extends RuntimeException {
    public MovePieceFailedException(String message) {
        super(message);
    }
}
