package chess.exception;

public class NoWinnerException extends IllegalArgumentException {
    public NoWinnerException(String message) {
        super(message);
    }

}
