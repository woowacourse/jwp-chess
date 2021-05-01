package chess.exception;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException(final String userName) {
        super(String.format("인증에 실패했습니다. (이름: %s)", userName));
    }

}
