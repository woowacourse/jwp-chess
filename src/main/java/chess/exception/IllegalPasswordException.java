package chess.exception;

public class IllegalPasswordException extends IllegalArgumentException {
    public IllegalPasswordException() {
        super("비밀번호가 틀렸습니다.");
    }
}
