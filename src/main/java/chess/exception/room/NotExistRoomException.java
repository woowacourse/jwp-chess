package chess.exception.room;

public class NotExistRoomException extends RoomException {

    public NotExistRoomException() {
        super("방이 존재하지 않습니다.");
    }
}
