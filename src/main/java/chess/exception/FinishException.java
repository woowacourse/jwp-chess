package chess.exception;

public class FinishException extends StateException {

    private static final String MESSAGE = "끝난 상태에서는 기물을 움직일 수 없습니다.";

    public FinishException() {
        super(MESSAGE);
    }
}
