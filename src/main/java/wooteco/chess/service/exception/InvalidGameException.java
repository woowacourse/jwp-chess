package wooteco.chess.service.exception;

public class InvalidGameException extends RuntimeException {
    public static final String INVALID_GAME_ID_MESSAGE = "존재하지 않는 게임 입니다.";

    public InvalidGameException() {
        super(INVALID_GAME_ID_MESSAGE);
    }
}
