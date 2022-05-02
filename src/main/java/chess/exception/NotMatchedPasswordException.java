package chess.exception;

public class NotMatchedPasswordException extends RuntimeException {

    private static final String NOT_MATCHED_ERROR = "비밀번호가 일치하지 않습니다.";

    public NotMatchedPasswordException() {
        super(NOT_MATCHED_ERROR);
    }
}
