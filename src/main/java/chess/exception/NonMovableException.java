package chess.exception;

public class NonMovableException extends ClientException {

    private static final String MESSAGE = "[ERROR] 선택한 기물은 해당 위치로 이동할 수 없습니다. ";

    public NonMovableException() {
        super(MESSAGE);
    }
}
