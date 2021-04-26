package chess.exception;

public class ValidTurnException extends StateException {

    private static final String MESSAGE = "본인 턴에 맞는 기물을 선택해 주세요.";

    public ValidTurnException() {
        super(MESSAGE);
    }
}
