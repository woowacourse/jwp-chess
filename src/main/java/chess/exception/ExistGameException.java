package chess.exception;

public class ExistGameException extends IllegalArgumentException {
    public ExistGameException() {
        super("동일한 이름의 게임이 이미 존재합니다");
    }
}
