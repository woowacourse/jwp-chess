package chess.exception;

public class NotMovableException extends RuntimeException {

    private static final String NOT_MOVABLE_MESSAGE = "해당 위치로 움직일 수 없습니다.";

    public NotMovableException() {
        super(NOT_MOVABLE_MESSAGE);
    }
}
