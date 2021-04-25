package chess.exception.room;

public class DuplicateRoomNameException extends RoomException {

    public DuplicateRoomNameException() {
        super("해당 이름의 방이 이미 존재합니다.");
    }
}
