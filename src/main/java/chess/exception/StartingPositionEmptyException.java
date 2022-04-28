package chess.exception;

public class StartingPositionEmptyException extends ClientException {

    private static final String MESSAGE = "[ERROR] 출발 위치에는 말이 있어야 합니다.";

    public StartingPositionEmptyException() {
        super(MESSAGE);
    }
}
