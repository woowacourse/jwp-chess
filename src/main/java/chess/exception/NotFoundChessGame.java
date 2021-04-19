package chess.exception;

public class NotFoundChessGame extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 체스 게임입니다.";

    public NotFoundChessGame() {
        super(MESSAGE);
    }

}
