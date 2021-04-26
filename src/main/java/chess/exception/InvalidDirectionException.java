package chess.exception;

public class InvalidDirectionException extends MovementException {

    private static final String MESSAGE = "유효하지 않은 방향입니다.";

    public InvalidDirectionException() {
        super(MESSAGE);
    }
}
