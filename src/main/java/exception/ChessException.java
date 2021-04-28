package exception;

public class ChessException extends RuntimeException {
    private final ExceptionStatus exceptionStatus;

    public ChessException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public int getHttpStatus() {
        return exceptionStatus.getHttpStatus();
    }

    public String getMessage() {
        return exceptionStatus.getMessage();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
