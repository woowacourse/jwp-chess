package chess.exception;

public class RunningStateException extends StateException {

    private static final String MESSAGE = "이미 진행중인 상태입니다.";

    public RunningStateException() {
        super(MESSAGE);
    }
}
