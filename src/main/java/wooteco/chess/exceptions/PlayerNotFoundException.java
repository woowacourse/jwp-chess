package wooteco.chess.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(final int id) {
        super(id + " 플레이어를 찾을 수 없습니다.");
    }
}
