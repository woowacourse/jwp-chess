package chess.exception;

public class GameJoinException extends RuntimeException {

    public GameJoinException() {
    }

    public GameJoinException(String message) {
        super(message);
    }
}
