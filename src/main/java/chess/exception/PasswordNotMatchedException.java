package chess.exception;

public class PasswordNotMatchedException extends RuntimeException {

    private static final String PASSWORD_NOT_MATCHED = "[ERROR] 비밀번호가 일치하지 않습니다";

    public PasswordNotMatchedException() {
        super(PASSWORD_NOT_MATCHED);
    }
}
