package chess.exception;

public class TurnOwnerException extends RuntimeException {
    public TurnOwnerException(String message) {
        super(message);
    }
}
