package chess.exception;

public class InvalidTurnException extends MovementException {

    private static final String MESSAGE = "상대턴 차례가 끝나지 않았습니다.";

    public InvalidTurnException() {
        super(MESSAGE);
    }
}
