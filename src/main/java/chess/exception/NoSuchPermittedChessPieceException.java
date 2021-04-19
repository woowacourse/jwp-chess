package chess.exception;

public class NoSuchPermittedChessPieceException extends ChessGameException {
    private static final String MESSAGE = "허용된 체스 말을 찾을 수 없습니다.";

    public NoSuchPermittedChessPieceException() {
        super(MESSAGE);
    }

}
