package chess.exception;

public class InvalidPasswordException extends IllegalArgumentException {

    public InvalidPasswordException() {
        super("비밀번호가 올바르지 않습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
