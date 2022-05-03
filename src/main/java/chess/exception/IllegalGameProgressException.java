package chess.exception;

public class IllegalGameProgressException extends IllegalArgumentException {
    public IllegalGameProgressException(String message) {
        super(message);
    }
}
