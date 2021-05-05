package chess.exception;

public class InvalidPasswordException extends UserException {

    private static final String MESSAGE = "잘못된 비밀번호입니다.";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
