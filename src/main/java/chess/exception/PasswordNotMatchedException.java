package chess.exception;

public class PasswordNotMatchedException extends DeleteChessGameException {
    public PasswordNotMatchedException() {
        super();
    }

    public PasswordNotMatchedException(String message) {
        super(message);
    }
}
