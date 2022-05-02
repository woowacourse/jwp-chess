package chess.exception;

public class RunningGameDeletionException extends IllegalArgumentException {

    public RunningGameDeletionException() {
        super("진행중인 게임은 삭제할 수 없습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
