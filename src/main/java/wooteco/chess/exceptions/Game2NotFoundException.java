package wooteco.chess.exceptions;

public class Game2NotFoundException extends RuntimeException {
    public Game2NotFoundException(final String id) {
        super(id + " 게임을 찾을 수 없습니다.");
    }
}
