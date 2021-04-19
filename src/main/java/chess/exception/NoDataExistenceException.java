package chess.exception;

public class NoDataExistenceException extends ChessGameException {
    private static final String MESSAGE = "데이터가 존재하지 않습니다.";

    public NoDataExistenceException() {
        super(MESSAGE);
    }

}
