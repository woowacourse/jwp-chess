package chess.exception;

public class UserNameNullException extends UserException {

    private static final String MESSAGE = "아이디를 입력해주세요.";

    public UserNameNullException() {
        super(MESSAGE);
    }
}
