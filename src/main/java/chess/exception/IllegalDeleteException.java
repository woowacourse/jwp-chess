package chess.exception;

public class IllegalDeleteException extends IllegalArgumentException {
    public IllegalDeleteException() {
        super("게임이 종료되어야 삭제가 가능합니다.");
    }
}
