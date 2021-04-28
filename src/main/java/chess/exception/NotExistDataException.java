package chess.exception;

public class NotExistDataException extends RuntimeException {

    public NotExistDataException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
