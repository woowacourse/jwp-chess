package chess.exception;

public class ChessException extends RuntimeException {
    private ErrorInformation error;

    public ChessException(ErrorInformation error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public int getStatusCode() {
        return this.error.getStatusCode();
    }

    public String getErrorMessage() {
        return this.error.getErrorMessage();
    }
}
