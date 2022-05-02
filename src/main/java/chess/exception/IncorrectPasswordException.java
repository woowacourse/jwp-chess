package chess.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("잘못된 비밀번호 입니다.");
    }
}
