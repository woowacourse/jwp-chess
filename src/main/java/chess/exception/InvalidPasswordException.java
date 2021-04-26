package chess.exception;

public class InvalidPasswordException extends IllegalArgumentException {
    public InvalidPasswordException() {
        super("비밀번호 인증에 실패했습니다.");
    }
}
