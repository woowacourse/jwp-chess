package chess.exception;

public class IncorrectPassword extends RuntimeException {

    public IncorrectPassword() {
        super("잘못된 비밀번호 입니다.");
    }
}
