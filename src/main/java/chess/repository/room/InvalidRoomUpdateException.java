package chess.repository.room;

public class InvalidRoomUpdateException extends RuntimeException {
    private static final String INVALID_ROOM_SAVE_EXCEPTION = "방을 저장할 수 없습니다.";

    public InvalidRoomUpdateException() {
        super(INVALID_ROOM_SAVE_EXCEPTION);
    }
}
