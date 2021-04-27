package chess.exception;

public class NotFoundChessGamePageException extends RuntimeException {
    private static final String MESSAGE = "해당 체스게임 페이지를 찾을 수 없습니다.";

    public NotFoundChessGamePageException() {
        super(MESSAGE);
    }

}
