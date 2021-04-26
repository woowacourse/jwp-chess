package chess.exception;

public class BlankMoveException extends BlankException {

    private static final String MESSAGE = "움직일 수 없습니다.";

    public BlankMoveException() {
        super(MESSAGE);
    }
}
