package chess.exception;

public class WrongAccessException extends ChessException {

    private static final String MESSAGE = "접근할 수 있는 사용자가 아닙니다.";

    public WrongAccessException() {
        super(MESSAGE);
    }
}
