package chess.exception;

public class AuthorizationException extends RuntimeException {

    private static final String MESSAGE = "로그인이 필요한 서비스입니다";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
