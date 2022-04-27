package chess.exception;

public class IllegalMoveException extends IllegalArgumentException {
    public IllegalMoveException(String message) {
        super(message);
    }
}
