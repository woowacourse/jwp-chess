package chess.exception;

public class DeleteChessGameException extends RuntimeException {
    public DeleteChessGameException() {
        super();
    }

    public DeleteChessGameException(String message) {
        super(message);
    }
}
