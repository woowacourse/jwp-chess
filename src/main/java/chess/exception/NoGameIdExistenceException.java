package chess.exception;

public class NoGameIdExistenceException extends ChessGameException {
    private static final String MESSAGE = "불러오기에 실패했습니다.";

    public NoGameIdExistenceException() {
        super(MESSAGE);
    }

}
