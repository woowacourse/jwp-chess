package wooteco.chess.domain.game.exception;

public class InvalidTurnException extends RuntimeException {
    private static final String INVALID_TURN_MESSAGE = "상대방의 턴입니다.";

    public InvalidTurnException() {
        super(INVALID_TURN_MESSAGE);
    }
}
