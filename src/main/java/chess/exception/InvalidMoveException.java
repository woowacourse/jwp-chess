package chess.exception;

public class InvalidMoveException extends MovementException {

    private static final String MESSAGE = "해당 위치로 이동할 수 없습니다.";

    public InvalidMoveException() {
        super(MESSAGE);
    }
}
