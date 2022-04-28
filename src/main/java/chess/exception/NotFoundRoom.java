package chess.exception;

public class NotFoundRoom extends RuntimeException {
    public NotFoundRoom() {
        super("해당하는 체스방을 찾을 수 없습니다.");
    }
}
