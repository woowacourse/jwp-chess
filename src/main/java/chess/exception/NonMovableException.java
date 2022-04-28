package chess.exception;

public class NonMovableException extends ClientException {

    private static final String MESSAGE = "[ERROR] 현재 올바르지 않은 팀 선택입니다. ";

    public NonMovableException() {
        super(MESSAGE);
    }
}
