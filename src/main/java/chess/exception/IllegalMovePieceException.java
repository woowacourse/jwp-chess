package chess.exception;

public class IllegalMovePieceException extends RuntimeException {

    public IllegalMovePieceException() {
    }

    public IllegalMovePieceException(String message) {
        super(message);
    }
}
