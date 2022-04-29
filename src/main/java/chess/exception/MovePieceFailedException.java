package chess.exception;

public class MovePieceFailedException extends RuntimeException {
    public MovePieceFailedException() {
        super("기물을 움직이는데 실패하였습니다.");
    }
}
