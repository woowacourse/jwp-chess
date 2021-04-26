package chess.exception;

public class UserPasswordNullException extends UserException{

    private static final String MESSAGE = "패스워드을 입력해주세요.";

    public UserPasswordNullException() {
        super(MESSAGE);
    }
}
