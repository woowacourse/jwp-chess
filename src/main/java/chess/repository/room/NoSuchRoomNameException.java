package chess.repository.room;

public class NoSuchRoomNameException extends RuntimeException {
    private static final String NO_SUCH_ROOM_NAME_EXCEPTION = "존재하지 않는 방 이름 입니다.";

    public NoSuchRoomNameException() {
        super(NO_SUCH_ROOM_NAME_EXCEPTION);
    }
}
