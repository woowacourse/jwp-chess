package chess.repository.room;

public class NoSuchRoomIdException extends RuntimeException {
    private static final String NO_SUCH_ROOM_ID_EXCEPTION = "존재하지 않는 방 id 입니다.";

    public NoSuchRoomIdException() {
        super(NO_SUCH_ROOM_ID_EXCEPTION);
    }
}
