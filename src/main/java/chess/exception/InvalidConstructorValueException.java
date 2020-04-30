package chess.exception;

public class InvalidConstructorValueException extends IllegalArgumentException {
    private static final String MESSAGE = "Null 혹은 유효하지 않은 값이 존재합니다.";

    public InvalidConstructorValueException() {
        super(MESSAGE);
    }
}
