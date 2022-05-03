package chess.utils.exception;

public class NoExecuteQuery extends RuntimeException {

    private static final String MESSAGE = "요청이 정상적으로 실행되지 않았습니다.";

    public NoExecuteQuery() {
        super(MESSAGE);
    }

}
