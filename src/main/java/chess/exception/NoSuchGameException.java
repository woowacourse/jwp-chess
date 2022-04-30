package chess.exception;

public class NoSuchGameException extends RuntimeException {

    public NoSuchGameException() {}

    public NoSuchGameException(String message) {
        super(message);
    }
}
