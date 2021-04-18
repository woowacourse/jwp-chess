package chess.repository.room;

public class DuplicateRoomNameException extends RuntimeException {
    private static final String DUPLICATE_ROOM_NAME_ERROR_MESSAGE = "존재하지 않는 방 이름입니다.";

    public DuplicateRoomNameException() {
        super(DUPLICATE_ROOM_NAME_ERROR_MESSAGE);
    }
}
