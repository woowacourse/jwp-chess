package chess.repository.room;

public class InvalidRoomDeleteException extends RuntimeException {
    private static final String UNABLE_TO_DELETE_ROOM_ERROR = "해당 방을 삭제 할 수 없습니다";

    public InvalidRoomDeleteException() {
        super(UNABLE_TO_DELETE_ROOM_ERROR);
    }
}
