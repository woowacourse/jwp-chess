package wooteco.chess.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(final String id) {
        super(id + " 게임을 찾을 수 없습니다.");
    }
}
