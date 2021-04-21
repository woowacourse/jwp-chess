package chess.exception;

public class NotFoundChessGameException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 체스 게임입니다.";

    public NotFoundChessGameException() {
        super(MESSAGE);
    }

}
