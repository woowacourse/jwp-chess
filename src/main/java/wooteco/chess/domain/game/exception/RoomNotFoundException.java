package wooteco.chess.domain.game.exception;

public class RoomNotFoundException extends RuntimeException {
    private static final String INVALID_ROOM_MESSAGE = "방을 찾을 수 없습니다.";

    public RoomNotFoundException() {
        super(INVALID_ROOM_MESSAGE);
    }
}
