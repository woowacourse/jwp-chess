package chess.exception;

public class ChessException extends RuntimeException {
    private ErrorCode errorCode;

    public ChessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int code() {
        return errorCode.getStatusCode();
    }

    public String desc() {
        return errorCode.getMessage();
    }
}
