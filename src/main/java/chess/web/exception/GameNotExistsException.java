package chess.web.exception;

public class GameNotExistsException extends RuntimeException {
    public GameNotExistsException() {
        super("게임이 존재하지 않습니다.");
    }
}
