package chess.exception;

public class DuplicatedNicknameException extends PlayerException {
    public DuplicatedNicknameException() {
        super("중복된 이름입니다.");
    }
}
