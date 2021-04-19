package chess.exception;

public class DataLoadException extends ChessGameException {
    private static final String MESSAGE = "불러오기에 실패했습니다.";

    public DataLoadException() {
        super(MESSAGE);
    }

}
