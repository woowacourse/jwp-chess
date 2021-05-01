package chess.domain.user;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException(final String password) {
        super(String.format("비밀번호가 틀렸습니다. 입력 비밀번호: %s", password));
    }

}
